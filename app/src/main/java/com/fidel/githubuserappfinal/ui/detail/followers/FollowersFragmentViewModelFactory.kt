package com.fidel.githubuserappfinal.ui.detail.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidel.githubuserappfinal.repository.Repository

class FollowersFragmentViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowersFragmentViewModel(repository) as T
    }
}