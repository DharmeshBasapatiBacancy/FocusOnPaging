package com.example.focusonpaging.network.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("full_name")
    val fullName: String)