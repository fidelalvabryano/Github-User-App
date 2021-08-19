package com.fidel.favoritelist.ui.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidel.favoritelist.R
import com.fidel.favoritelist.databinding.ActivityMainBinding
import com.fidel.favoritelist.model.UserData
import com.fidel.favoritelist.repository.Repository
import com.fidel.favoritelist.ui.detail.DetailActivity
import com.fidel.favoritelist.ui.favorite.FavoriteActivity
import com.fidel.favoritelist.ui.notif.NotifActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listUser: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Search User"

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        adapter = UserAdapter(listUser)
        binding.progressBar.visibility = View.INVISIBLE
        binding.recycleView.layoutManager = LinearLayoutManager(recycleView.context)
        binding.recycleView.setHasFixedSize(true)
        retrofitGetStartingUsers()
    }

    private fun retrofitGetStartingUsers() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getUsers()
        viewModel.usersResponse.observe(this, Observer { usersResponse ->
            if (usersResponse.isSuccessful) {
                usersResponse.body()?.forEach{
                    val responseLogin = it.login.toString()
                    retrofitGetUsersDetail(responseLogin)
                    Log.d("Get Users Response", it.login.toString())
                    Log.d("Get Users Response", "============================")
                }
            } else {
                Log.d("Get Users Response", usersResponse.errorBody().toString())
                Toast.makeText(this, usersResponse.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("LongLogTag")
    private fun retrofitGetUsersDetail(login: String) {
        viewModel.getUsersDetail(login)
        viewModel.usersDetailResponse.observe(this, Observer { usersDetailResponse ->
            if (usersDetailResponse.isSuccessful) {
                binding.progressBar.visibility = View.INVISIBLE
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.login.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.name.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.avatar_url.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.company.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.location.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.public_repos.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.followers.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.followers.toString())
                Log.d("Get Users Detail Response", "-------------------------")
                if (login == usersDetailResponse.body()?.login) {
                    listUser.add(
                        UserData(
                            usersDetailResponse.body()?.login,
                            usersDetailResponse.body()?.name,
                            usersDetailResponse.body()?.avatar_url,
                            usersDetailResponse.body()?.company,
                            usersDetailResponse.body()?.location,
                            usersDetailResponse.body()?.public_repos,
                            usersDetailResponse.body()?.followers,
                            usersDetailResponse.body()?.following)
                    )
                }
                showUserList()
            } else {
                Log.d("Get Users Detail Response", usersDetailResponse.errorBody().toString())
                Toast.makeText(this, usersDetailResponse.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("LongLogTag")
    private fun retrofitGetUsersSearchDetail(login: String) {
        viewModel.getUsersDetail(login)
        viewModel.usersDetailResponse.observe(this, Observer { usersDetailResponse ->
            if (usersDetailResponse.isSuccessful) {
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.login.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.name.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.avatar_url.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.company.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.location.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.public_repos.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.followers.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.followers.toString())
                Log.d("Get Users Detail Response", "-------------------------")
                if (login == usersDetailResponse.body()?.login) {
                    listUser.add(
                        UserData(
                            usersDetailResponse.body()?.login,
                            usersDetailResponse.body()?.name,
                            usersDetailResponse.body()?.avatar_url,
                            usersDetailResponse.body()?.company,
                            usersDetailResponse.body()?.location,
                            usersDetailResponse.body()?.public_repos,
                            usersDetailResponse.body()?.followers,
                            usersDetailResponse.body()?.following)
                    )
                }
                showUserList()
            } else {
                Log.d("Get Users Detail Response", usersDetailResponse.errorBody().toString())
                Toast.makeText(this, usersDetailResponse.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun retrofitGetUsersSearch(login: String) {
        viewModel.getUsersSearch(login)
        listUser.clear()
        viewModel.usersSearchResponse.observe(this, Observer { usersSearchResponse ->
            if (usersSearchResponse.isSuccessful) {
                Log.d("Users Search Response", usersSearchResponse.body().toString())
                val items = usersSearchResponse.body()?.items
                Log.d("User Search Response", items.toString())
                items?.forEach {
                    val responseLogin = it.login.toString()
                    retrofitGetUsersSearchDetail(responseLogin)
                }
            } else {
                Toast.makeText(this, usersSearchResponse.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showUserList() {
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = UserAdapter(listUser)
        binding.recycleView.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: UserData) {
                showSelectedUser(dataUsers)
            }
        })
    }

    private fun showSelectedUser(dataUser: UserData) {
        UserData(
            dataUser.username,
            dataUser.name,
            dataUser.avatar,
            dataUser.company,
            dataUser.location,
            dataUser.repository,
            dataUser.followers,
            dataUser.following
        )
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, dataUser)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isBlank()) {
                    return true
                } else {
                    binding.recycleView.visibility = View.VISIBLE
                    listUser.clear()
                    retrofitGetUsersSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_change_settings -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.action_change_notification -> {
                val intent = Intent(this, NotifActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}