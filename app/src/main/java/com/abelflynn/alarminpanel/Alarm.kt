package com.abelflynn.alarminpanel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

class Alarm(private val context: Context) {
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val daysOfWeekAlarms = arrayOfNulls<PendingIntent>(7)

    fun set(hour: Int, minute: Int, daysOfWeek: BooleanArray) {
        val orderedDaysOfWeek = arrayOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
        for (index in orderedDaysOfWeek.indices) {
            val day = orderedDaysOfWeek[index]
            if (daysOfWeek[index]) {
                val intent = Intent(context, AlarmReceiver::class.java)
                daysOfWeekAlarms[index] = PendingIntent.getBroadcast(context, day, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, day)
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DAY_OF_YEAR, 7)
                    }
                }

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, daysOfWeekAlarms[index])
                Log.d("AlarmApp", "Alarm set for day $day at time ${calendar.time}")
            } else {
                daysOfWeekAlarms[index]?.let {
                    alarmManager.cancel(it)
                    daysOfWeekAlarms[index] = null
                    Log.d("AlarmApp", "Alarm cancelled for day $day")
                }
            }
        }
    }

    fun cancel() {
        for (index in daysOfWeekAlarms.indices) {
            daysOfWeekAlarms[index]?.let {
                alarmManager.cancel(it)
                daysOfWeekAlarms[index] = null
                Log.d("AlarmApp", "Alarm cancelled for day $index")
            }
        }
    }
}
