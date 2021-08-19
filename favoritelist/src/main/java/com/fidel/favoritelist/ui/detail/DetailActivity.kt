package com.fidel.favoritelist.ui.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide
import com.fidel.favoritelist.R
import com.fidel.favoritelist.databinding.ActivityDetailBinding
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.AVATAR
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.COMPANY
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.FAVORITE
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.LOCATION
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.NAME
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.REPOSITORY
import com.fidel.favoritelist.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.fidel.favoritelist.db.FavoriteHelper
import com.fidel.favoritelist.model.Favorite
import com.fidel.favoritelist.model.UserData
import com.fidel.favoritelist.ui.notif.NotifActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE_USER = "extra_favorite_user"
        const val EXTRA_FAVORITE_LIST = "extra_favorite_list"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding: ActivityDetailBinding
    private var favState = false
    private lateinit var favoriteHelper: FavoriteHelper
    private var favorites: Favorite? = null
    private lateinit var imageAvatar: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        favorites = intent.getParcelableExtra(EXTRA_FAVORITE_LIST)
        if (favorites != null) {
            setUserFavoriteDetail()
        } else {
            setUserDetail()
        }

        viewPagerConfig()
        binding.btnFavorite.setOnClickListener{
            if (!favState) {
                val dataUser = intent.getParcelableExtra(EXTRA_USER) as UserData?
                val values = ContentValues()
                values.put(USERNAME, dataUser?.username.toString())
                values.put(NAME, dataUser?.name.toString())
                values.put(AVATAR, dataUser?.avatar.toString())
                values.put(COMPANY, dataUser?.company.toString())
                values.put(LOCATION, dataUser?.location.toString())
                values.put(REPOSITORY, dataUser?.repository.toString())
                values.put(FAVORITE, "1")
                contentResolver.insert(CONTENT_URI, values)
                binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(this, getString(R.string.add_fav_msg, dataUser?.name), Toast.LENGTH_SHORT).show()
                favState = true
            } else {
                favoriteHelper.deleteById(favorites?.username.toString())
                binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(this, getString(R.string.delete_fav_msg, favorites?.name), Toast.LENGTH_SHORT).show()
                favState = false
            }
        }
        binding.btnShare.setOnClickListener {
            val dataUser = intent.getParcelableExtra(EXTRA_USER) as UserData?
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(getString(R.string.share))
                .setText(getString(R.string.share_msg, dataUser?.name.toString(), dataUser?.username.toString()))
                .startChooser()
        }
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = viewPagerDetailAdapter
        binding.tabLayout.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    private fun setUserDetail() {
        val dataUser = intent.getParcelableExtra(EXTRA_USER) as UserData?
        supportActionBar?.title = dataUser?.name
        binding.detailTxtName.text = getString(R.string.name, dataUser?.name)
        binding.detailTxtUsername.text = getString(R.string.username, dataUser?.username)
        binding.detailTxtCompany.text = getString(R.string.company, dataUser?.company)
        binding.detailTxtLocation.text = getString(R.string.location, dataUser?.location)
        binding.detailTxtRepository.text = getString(R.string.repository, dataUser?.repository)
        Glide.with(this)
            .load(dataUser?.avatar)
            .into(binding.detailImgAvatar)
        imageAvatar = dataUser?.avatar.toString()
        favState = false
    }

    private fun setUserFavoriteDetail() {
        val favoriteUser = intent.getParcelableExtra(EXTRA_FAVORITE_LIST) as Favorite?
        supportActionBar?.title = favoriteUser?.name
        detail_txt_name.text = favoriteUser?.name
        detail_txt_username.text = favoriteUser?.username
        detail_txt_company.text = favoriteUser?.company
        detail_txt_location.text = favoriteUser?.location
        detail_txt_repository.text = favoriteUser?.repository
        Glide.with(this)
            .load(favoriteUser?.avatar)
            .into(detail_img_avatar)
        imageAvatar = favoriteUser?.avatar.toString()
        binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        favState = true
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.removeItem(R.id.search)
        menu.removeItem(R.id.action_favorite)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.action_change_notification -> {
                val intent = Intent(this, NotifActivity::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
