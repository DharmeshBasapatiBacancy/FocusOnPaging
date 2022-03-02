package com.example.focusonpaging.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.focusonpaging.network.model.Hit
import com.example.focusonpaging.network.model.Repo
import com.example.focusonpaging.paging.MyRepository
import kotlinx.coroutines.flow.Flow

class MyViewModel(private val repository: MyRepository): ViewModel() {

    private var currentUsernameValue: String? = null

    private var currentSearchResult: Flow<PagingData<Hit>>? = null


    fun searchRepos(username: String): Flow<PagingData<Hit>> {
        val lastResult = currentSearchResult
        if (username == currentUsernameValue && lastResult != null) {
            return lastResult
        }
        currentUsernameValue = username
        val newResult = repository.searchRepos(username)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}