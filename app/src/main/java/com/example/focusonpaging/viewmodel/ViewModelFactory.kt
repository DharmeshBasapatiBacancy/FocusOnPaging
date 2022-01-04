package com.example.focusonpaging.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.focusonpaging.paging.MyRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repository: MyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyViewModel::class.java)){
            return MyViewModel(repository) as T
        }
        throw IllegalArgumentException("Illegal Class Name")
    }
}