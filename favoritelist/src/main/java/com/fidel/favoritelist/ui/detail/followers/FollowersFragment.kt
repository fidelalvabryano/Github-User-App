package com.fidel.favoritelist.ui.detail.followers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidel.favoritelist.databinding.FragmentFollowersBinding
import com.fidel.favoritelist.model.Favorite
import com.fidel.favoritelist.model.UserData
import com.fidel.favoritelist.repository.Repository
import com.fidel.favoritelist.ui.detail.DetailActivity
import com.fidel.favoritelist.ui.search.mContext

class FollowersFragment : Fragment() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE_LIST = "extra_favorite_list"
    }

    private var listUser: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: FollowersAdapter
    private var favorites: Favorite? = null
    private lateinit var dataFavorite: Favorite
    private var dataUser: UserData? = null
    private lateinit var viewModel: FollowersFragmentViewModel
    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowersAdapter(listUser)
        listUser.clear()

        val repository = Repository()
        val followersFragmentViewModelFactory = FollowersFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, followersFragmentViewModelFactory).get(
            FollowersFragmentViewModel::class.java)

        favorites = requireActivity().intent.getParcelableExtra(DetailActivity.EXTRA_FAVORITE_LIST)
        if (favorites != null) {
            dataFavorite = requireActivity().intent.getParcelableExtra<Favorite>(EXTRA_FAVORITE_LIST) as Favorite
            retrofitGetUserFollowers(dataFavorite.username.toString())
        } else {
            dataUser = requireActivity().intent.getParcelableExtra(EXTRA_USER)
            retrofitGetUserFollowers(dataUser?.username.toString())
        }
    }

    @SuppressLint("LongLogTag")
    private fun retrofitGetUserFollowers(login: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getUserFollowers(login)
        viewModel.myResponse2.observe(viewLifecycleOwner, Observer { userFollowersResponse ->
            if (userFollowersResponse.isSuccessful) {
                userFollowersResponse.body()?.forEach {
                    val userFollowersLoginResponse = it.login.toString()
                    retrofitGetUsersDetail(userFollowersLoginResponse)
                }
            } else {
                Log.d("Get User Followers Response", userFollowersResponse.errorBody().toString())
                Toast.makeText(context, userFollowersResponse.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("LongLogTag")
    private fun retrofitGetUsersDetail(login: String) {
        viewModel.getUsersDetail(login)
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { usersDetailResponse ->
            if (usersDetailResponse.isSuccessful) {
                binding.progressBar.visibility = View.INVISIBLE
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.name.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.login.toString())
                Log.d("Get Users Detail Response", usersDetailResponse.body()?.public_repos.toString())
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
                showFollowersList()
            } else {
                Log.d("Get Users Detail Response", usersDetailResponse.errorBody().toString())
                Toast.makeText(context, usersDetailResponse.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showFollowersList() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter = FollowersAdapter(followersList)
        binding.rvFollowers.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object :
            FollowersAdapter.OnItemClickCallback {
            override fun onItemClicked(UserData: UserData) {
                val dataUser = UserData(
                    dataUser?.username,
                    dataUser?.name,
                    dataUser?.avatar,
                    dataUser?.company,
                    dataUser?.location,
                    dataUser?.repository,
                    dataUser?.followers,
                    dataUser?.following,
                    dataUser?.isFav
                )
                val intentDetail = Intent(mContext, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_USER, dataUser)
                intentDetail.putExtra(DetailActivity.EXTRA_FAVORITE_USER, dataUser)
                mContext.startActivity(intentDetail)
            }
        })
    }
}