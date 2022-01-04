package com.example.focusonpaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusonpaging.adapter.RepoAdapter
import com.example.focusonpaging.adapter.ReposLoadStateAdapter
import com.example.focusonpaging.databinding.ActivityMainBinding
import com.example.focusonpaging.network.service.MyApiService
import com.example.focusonpaging.paging.MyRepository
import com.example.focusonpaging.viewmodel.MyViewModel
import com.example.focusonpaging.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repoAdapter: RepoAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        setupViewModel()
    }

    private fun setupUI() {
        repoAdapter = RepoAdapter()
        binding.rvRepos.layoutManager = LinearLayoutManager(this)
        binding.rvRepos.adapter = repoAdapter.withLoadStateFooter(footer = ReposLoadStateAdapter())
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,
            ViewModelFactory(MyRepository(MyApiService.create())))
            .get(MyViewModel::class.java)

        lifecycleScope.launch {

            viewModel.searchRepos("google").collect {
                repoAdapter.submitData(it)
            }
        }

    }
}