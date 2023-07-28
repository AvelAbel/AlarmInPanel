package com.abelflynn.alarminpanel

import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var timePicker: TimePicker
    private lateinit var alarm: Alarm
    private val daysOfWeek = BooleanArray(7)

    private val requestExactAlarmPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val hour = timePicker.hour
                val minute = timePicker.minute
                alarm.set(hour, minute, daysOfWeek)
            } else {
                // Пользователь не предоставил разрешение
                Log.e("AlarmApp", "Не удалось получить разрешение SCHEDULE_EXACT_ALARM")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvAlarmInfo = findViewById<TextView>(R.id.tvAlarmInfo)

        val daysOfWeekNames = arrayOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

        alarm = Alarm(this)
        timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        val btnCancelAlarm = findViewById<Button>(R.id.btnCancelAlarm)

        val btnMonday = findViewById<Button>(R.id.btnMonday)
        val btnTuesday = findViewById<Button>(R.id.btnTuesday)
        val btnWednesday = findViewById<Button>(R.id.btnWednesday)
        val btnThursday = findViewById<Button>(R.id.btnThursday)
        val btnFriday = findViewById<Button>(R.id.btnFriday)
        val btnSaturday = findViewById<Button>(R.id.btnSaturday)
        val btnSunday = findViewById<Button>(R.id.btnSunday)

        val dayButtons = arrayOf(btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday)

        for (i in dayButtons.indices) {
            val button = dayButtons[i]
            button.setOnClickListener {
                daysOfWeek[i] = !daysOfWeek[i]
                button.alpha = if (daysOfWeek[i]) 1.0f else 0.3f
            }
        }

        btnSetAlarm.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent("android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION")
                    requestExactAlarmPermissionLauncher.launch(intent)
                } else {
                    alarm.set(hour, minute, daysOfWeek)
                }
            } else {
                alarm.set(hour, minute, daysOfWeek)
            }

            val selectedDays = daysOfWeek.withIndex().filter { it.value }.map { daysOfWeekNames[it.index] }.joinToString(", ")

            tvAlarmInfo.text = "Будильник сработает в ${String.format("%02d:%02d", hour, minute)} ($selectedDays)"
        }

        btnCancelAlarm.setOnClickListener {
            alarm.cancel()
        }
    }
}
