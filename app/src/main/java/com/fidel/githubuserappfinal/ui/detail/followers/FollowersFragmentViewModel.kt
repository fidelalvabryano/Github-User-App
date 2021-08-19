package com.fidel.githubuserappfinal.ui.detail.followers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fidel.githubuserappfinal.model.Post
import com.fidel.githubuserappfinal.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class FollowersFragmentViewModel(private val repository: Repository): ViewModel() {

    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()
    val myResponse2: MutableLiveData<Response<List<Post>>> = MutableLiveData()

    fun getUsersDetail(login: String) {
        viewModelScope.launch {
            val response = repository.getUsersDetail(login)
            myResponse.value = response
        }
    }

    fun getUserFollowers(login: String) {
        viewModelScope.launch {
            val response = repository.getUserFollowers(login)
            myResponse2.value = response
        }
    }



}