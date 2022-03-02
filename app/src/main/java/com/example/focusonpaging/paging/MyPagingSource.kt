package com.example.focusonpaging.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.focusonpaging.network.model.Hit
import com.example.focusonpaging.network.service.MyApiService

private const val INITIAL_PAGE = 1

class MyPagingSource(
    private val api: MyApiService,
    private val searchQuery: String,
) : PagingSource<Int, Hit>() {
    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = api.searchProduct(query = searchQuery, pageNo = page)
            LoadResult.Page(
                data = response.body()?.hits!!,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.body()?.hits?.isEmpty()!!) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}