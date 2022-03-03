package com.example.focusonpaging

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusonpaging.adapter.RepoAdapter
import com.example.focusonpaging.adapter.ReposLoadStateAdapter
import com.example.focusonpaging.databinding.ActivityMainBinding
import com.example.focusonpaging.databinding.HeaderFilterProductsBinding
import com.example.focusonpaging.databinding.HeaderMainDrawerBinding
import com.example.focusonpaging.network.service.MyApiService
import com.example.focusonpaging.paging.MyRepository
import com.example.focusonpaging.viewmodel.MyViewModel
import com.example.focusonpaging.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repoAdapter: RepoAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MyViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        binding.imgMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            binding.nvView.removeHeaderView(binding.nvView.getHeaderView(0))
            binding.nvView.addHeaderView(HeaderMainDrawerBinding.inflate(layoutInflater).root)
        }

        binding.btnFilter.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            binding.nvView.removeHeaderView(binding.nvView.getHeaderView(0))
            binding.nvView.addHeaderView(HeaderFilterProductsBinding.inflate(layoutInflater).root)
        }

        setupViewModel()

        filterListWithSearch()

        updateListOnSelectAll()
    }

    private fun updateListOnSelectAll() {
        binding.checkboxSelectAll.setOnClickListener {
            repoAdapter.updateCheckBoxes(binding.checkboxSelectAll.isChecked)
            if (binding.checkboxSelectAll.isChecked) {
                binding.btnLayouts.visibility = View.VISIBLE
            } else {
                binding.btnLayouts.visibility = View.GONE
            }
        }
    }

    private fun filterListWithSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "onTextChanged: $p0")
                if (p0?.length!! > 2) {
                    fetchProducts(p0.toString())
                } else {
                    fetchProducts("*")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    private fun fetchProducts(query: String) {
        lifecycleScope.launch {
            viewModel.searchRepos(query).collect {
                repoAdapter.submitData(it)
            }
        }
    }

    private fun setupUI() {
        repoAdapter = RepoAdapter()
        binding.rvRepos.layoutManager = LinearLayoutManager(this)
        binding.rvRepos.adapter = repoAdapter.withLoadStateFooter(footer = ReposLoadStateAdapter())
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(MyRepository(MyApiService.create()))
        ).get(MyViewModel::class.java)
        fetchProducts("*")
    }
}