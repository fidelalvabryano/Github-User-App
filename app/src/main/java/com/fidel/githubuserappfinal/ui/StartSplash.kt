package com.fidel.githubuserappfinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fidel.githubuserappfinal.R
import com.fidel.githubuserappfinal.ui.search.MainActivity

class StartSplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_splash)
        supportActionBar?.hide()
        Handler(mainLooper).postDelayed({
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
            finish()
        }, 2000)
    }
}