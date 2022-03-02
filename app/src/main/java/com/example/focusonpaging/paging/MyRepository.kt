package com.example.focusonpaging.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.focusonpaging.network.service.MyApiService

class MyRepository(private val api: MyApiService) {

    fun searchRepos(query: String) = Pager(
        pagingSourceFactory = { MyPagingSource(api, query) },
        config = PagingConfig(
            pageSize = 10
        )
    ).flow

}