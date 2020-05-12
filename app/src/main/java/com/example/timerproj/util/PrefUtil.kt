package com.example.timerproj.util

import android.content.Context
import android.preference.PreferenceManager
import java.util.prefs.PreferenceChangeEvent

class PrefUtil {
    companion object {
        fun getTimerLength(context: Context): Int {
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.rescoder.timer.prevois_timer_length"
        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, defValue:0)
        }

        fun setPreviousTimerLenghtSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.rescoder.timer.timer_state"

        fun getTimerState(context: Context): ActivitateTimer.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, defValue=0)
            return ActivitateTimer.TimerState.values()[ordinal]
        }

        fun setTimerState(state: ActivitateTimer.TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()

        }

        private const val SECONDS_REMAINING_ID = "com.rescoder.timer.seconds_remaining"
        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, defValue=0)
        }
        fun setSecondsRemaining(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

    }
}