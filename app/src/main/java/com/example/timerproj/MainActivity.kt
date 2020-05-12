package com.example.timerproj

import android.content.IntentSender
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.timerproj.util.PrefUtil

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_first.*

class MainActivity : AppCompatActivity() {
    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState= TimerState.Stopped

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_av)
        supportActionBar?.title = "     Timer"

        fab_play.setOnClickListener { v ->
            startTimer()
                 TimerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener { v->
            timer.cancel()
            timerState = TimerState.Paused
        }

        fab_stop.setOnClickListener { v->
            timer.cancel()
            onTimerFinished()
        }

        private fun initTimer() {
            timerState = PrefUtil.getTimerState(context = this)

            if(timerState == TimerState.Stopped)
                setNewTimerLength()
            else
                setPreviousTimerLength()

            secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
                prefUtil.getSecondsRemaining(context = this)
            else
                timerLengthSeconds

            if(timerState == TimerState.Running)
                startTimer()

            updateButtons()
            updateCountdownUI()
        }

        override fun onResume() {
            super.onResume()

            initTimer()

            //sters bkg
        }

        override fun onPause() {
            super.onPause()

            if(timerState == TimerState.Running) {
                timer.cancel()
                //start bkg si afisare notif
            } else if (timerState == TimerState.Paused) {
                //afisare notificari
            }


            PrefUtil.setPreviousTimerLenghtSeconds(timerLengthSeconds)
            PrefUtil.setSecondsRemaining(secondsRemaining, context = this)
            PrefUtil.setTimerState(timerState, context = this)
        }



    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = (secondsRemaining / 60)
        val minutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
        var secondsStr = secondsInMinutesUntilFinished.toString()

        textView_countdown.text = "$minutesUntilFinished:${
        if (secondsStr.lenth == 2) secondsStr
        else "0" + secondsStr}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped

        setNewTimerLength()

        progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, context = this)
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()

    }

    private var secondsRemaining = 0L
    private fun startTimer() {
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000 ) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(milisUntilFinished: Long) {
                secondsRemaining = milisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(context = this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(context = this)
        progress_countdown.max = timerLengthSeconds.toInt()
    }



    private fun updateButtons() {
        when (timerState) {
            TimerState.Running ->{
                fab_play.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }

            TimerState.Stopped ->{
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }

            TimerState.Paused ->{
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
