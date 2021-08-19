package com.fidel.favoritelist.ui.detail.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidel.favoritelist.repository.Repository

class FollowingFragmentViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowingFragmentViewModel(repository) as T
    }
}