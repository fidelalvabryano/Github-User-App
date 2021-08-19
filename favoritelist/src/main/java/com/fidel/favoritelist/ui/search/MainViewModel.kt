package com.fidel.favoritelist.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fidel.favoritelist.model.Post
import com.fidel.favoritelist.model.SearchUser
import com.fidel.favoritelist.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val usersResponse: MutableLiveData<Response<List<Post>>> = MutableLiveData()
    val usersDetailResponse: MutableLiveData<Response<Post>> = MutableLiveData()
    val usersSearchResponse: MutableLiveData<Response<SearchUser>> = MutableLiveData()

    fun getUsers() {
        viewModelScope.launch {
            val response = repository.getUsers()
            usersResponse.value = response
        }
    }

    fun getUsersDetail(login: String) {
        viewModelScope.launch {
            val response = repository.getUsersDetail(login)
            usersDetailResponse.value = response
        }
    }

    fun getUsersSearch(login: String) {
        viewModelScope.launch {
            val response = repository.getUsersSearch(login)
            usersSearchResponse.value = response
        }
    }

}