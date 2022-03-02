package com.example.focusonpaging.network.model

data class Document(
    val barcode: String,
    val brand_name: String,
    val handle: String,
    val inventory_level: String,
    val name: String,
    val price_including_tax: Double,
    val reorder_point: String,
    val sku: String,
    val stock_status: String
)