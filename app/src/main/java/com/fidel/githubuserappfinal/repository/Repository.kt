package com.fidel.githubuserappfinal.repository

import com.fidel.githubuserappfinal.api.RetrofitInstance
import com.fidel.githubuserappfinal.model.Post
import com.fidel.githubuserappfinal.model.SearchUser
import retrofit2.Response

class Repository {

    suspend fun getUsers(): Response<List<Post>> {
        return RetrofitInstance.api.getUsers()
    }

    suspend fun getUsersDetail(login: String): Response<Post> {
        return RetrofitInstance.api.getUsersDetail(login)
    }

    suspend fun getUsersSearch(login: String): Response<SearchUser> {
        return RetrofitInstance.api.getUsersSearch(login)
    }

    suspend fun getUserFollowers(login: String): Response<List<Post>> {
        return RetrofitInstance.api.getUserFollowers(login)
    }

    suspend fun getUserFollowing(login: String): Response<List<Post>> {
        return RetrofitInstance.api.getUserFollowing(login)
    }

}