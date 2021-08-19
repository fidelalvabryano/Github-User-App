package com.fidel.favoritelist.ui.notif

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fidel.favoritelist.R
import com.fidel.favoritelist.databinding.ActivityNotifBinding

class NotifActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "SettingPref"
        private const val DAILY = "daily"
        private const val TIME = "09:00"
    }

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityNotifBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification Setting"

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        binding.switchBtn.isChecked = sharedPreferences.getBoolean(DAILY, false)

        binding.switchBtn.setOnCheckedChangeListener { _, isActive ->
            if (isActive) {
                alarmReceiver.setDailyReminder(
                    this,
                    AlarmReceiver.TYPE_DAILY,
                    TIME,
                    getString(R.string.notif_msg)
                )
            } else {
                alarmReceiver.cancelAlarm(this)
            }
            val editor = sharedPreferences.edit()
            editor.putBoolean(DAILY, isActive)
            editor.apply()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}