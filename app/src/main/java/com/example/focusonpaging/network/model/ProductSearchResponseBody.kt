package com.example.focusonpaging.network.model

data class ProductSearchResponseBody(
    val found: Int,
    val hits: List<Hit>,
    val out_of: Int,
    val page: Int,
)