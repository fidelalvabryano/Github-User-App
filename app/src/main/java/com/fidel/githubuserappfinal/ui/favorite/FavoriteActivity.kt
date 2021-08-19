package com.fidel.githubuserappfinal.ui.favorite

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidel.githubuserappfinal.R
import com.fidel.githubuserappfinal.databinding.ActivityFavoriteBinding
import com.fidel.githubuserappfinal.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.fidel.githubuserappfinal.db.FavoriteHelper
import com.fidel.githubuserappfinal.db.MappingHelper
import com.fidel.githubuserappfinal.model.Favorite
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Your Favorite"
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvFavorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val favoriteList = deferredFavorites.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (favoriteList.size > 0) {
                adapter.listFavorite = favoriteList
            } else {
                adapter.listFavorite = ArrayList()
                Toast.makeText(this@FavoriteActivity, getString(R.string.empty_msg), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onResume() {
        super.onResume()
        loadFavoritesAsync()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
