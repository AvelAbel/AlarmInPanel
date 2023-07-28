package com.abelflynn.alarminpanel

import android.app.Activity
import android.media.RingtoneManager
import android.media.AudioAttributes
import android.media.Ringtone
import android.os.Bundle
import android.widget.Button

class AlarmActivity : Activity() {
    private lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, alarmUri).apply {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            this.audioAttributes = attributes
            play()
        }

        val stopButton = findViewById<Button>(R.id.stop_button)
        stopButton.setOnClickListener {
            ringtone.stop()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone.stop()
    }
}
