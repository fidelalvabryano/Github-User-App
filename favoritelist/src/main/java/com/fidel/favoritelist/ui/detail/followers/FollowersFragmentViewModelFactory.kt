package com.fidel.favoritelist.ui.detail.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidel.favoritelist.repository.Repository

class FollowersFragmentViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowersFragmentViewModel(repository) as T
    }
}