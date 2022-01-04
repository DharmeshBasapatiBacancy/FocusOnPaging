package com.example.focusonpaging.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.focusonpaging.network.service.MyApiService

class MyRepository(private val api: MyApiService) {

    fun searchRepos(username: String) = Pager(
        pagingSourceFactory = { MyPagingSource(api, username) },
        config = PagingConfig(
            pageSize = 20
        )
    ).flow

}