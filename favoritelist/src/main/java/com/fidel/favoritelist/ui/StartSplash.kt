package com.fidel.favoritelist.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fidel.favoritelist.R
import com.fidel.favoritelist.ui.favorite.FavoriteActivity

class StartSplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_splash)
        supportActionBar?.hide()
        Handler(mainLooper).postDelayed({
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}