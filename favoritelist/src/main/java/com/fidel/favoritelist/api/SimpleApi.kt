package com.fidel.favoritelist.api

import com.fidel.favoritelist.model.Post
import com.fidel.favoritelist.model.SearchUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpleApi {

    @GET("/users")
    suspend fun getUsers(): Response<List<Post>>

    @GET("/users/{login}")
    suspend fun getUsersDetail(
        @Path("login") login: String
    ): Response<Post>

    @GET("/search/users")
    suspend fun getUsersSearch(
        @Query("q") login: String
    ): Response<SearchUser>

    @GET("/users/{login}/followers")
    suspend fun getUserFollowers(
        @Path("login") login: String
    ): Response<List<Post>>

    @GET("/users/{login}/following")
    suspend fun getUserFollowing(
        @Path("login") login: String
    ): Response<List<Post>>

}