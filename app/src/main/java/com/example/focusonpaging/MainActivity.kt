package com.example.focusonpaging

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusonpaging.adapter.RepoAdapter
import com.example.focusonpaging.adapter.ReposLoadStateAdapter
import com.example.focusonpaging.databinding.ActivityMainBinding
import com.example.focusonpaging.databinding.HeaderFilterProductsBinding
import com.example.focusonpaging.databinding.HeaderMainDrawerBinding
import com.example.focusonpaging.databinding.ItemProductsBinding
import com.example.focusonpaging.network.service.MyApiService
import com.example.focusonpaging.paging.MyRepository
import com.example.focusonpaging.utils.SwipeController
import com.example.focusonpaging.viewmodel.MyViewModel
import com.example.focusonpaging.viewmodel.ViewModelFactory
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener.OnRowClickListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), RecyclerTouchListener.RecyclerTouchListenerHelper {

    private var touchListener: OnActivityTouchListener? = null
    private lateinit var onTouchListener: RecyclerTouchListener
    private lateinit var swipeController: SwipeController
    private var swipeBack: Boolean = false
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
                //binding.checkboxSelectAll.text = "Unselect All"
                binding.btnLayouts.visibility = View.VISIBLE
            } else {
                //binding.checkboxSelectAll.text = "Select All"
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
        binding.rvRepos.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        binding.rvRepos.adapter = repoAdapter.withLoadStateFooter(footer = ReposLoadStateAdapter())

        onTouchListener = RecyclerTouchListener(this, binding.rvRepos)
        onTouchListener
            .setClickable(object : OnRowClickListener {
                override fun onRowClicked(position: Int) {

                }

                override fun onIndependentViewClicked(independentViewID: Int, position: Int) {

                }
            })
            .setSwipeOptionViews(
                ItemProductsBinding.inflate(layoutInflater).printLabel.id,
                ItemProductsBinding.inflate(layoutInflater).deRange.id
            )
            .setSwipeable(
                ItemProductsBinding.inflate(layoutInflater).rowFG.id,
                ItemProductsBinding.inflate(layoutInflater).rowBG.id
            ) { viewID, position ->
                var message = ""
                if (viewID == ItemProductsBinding.inflate(layoutInflater).printLabel.id) {
                    message += "Print Label"
                } else if (viewID == ItemProductsBinding.inflate(layoutInflater).deRange.id) {
                    message += "De-Range"
                }
                message += " clicked for row " + (position + 1) + "Name = $repoAdapter.get"
                Log.d(TAG, "setupUI: $message")

            }

    }

    override fun onResume() {
        super.onResume()
        binding.rvRepos.addOnItemTouchListener(onTouchListener)
    }

    override fun onPause() {
        super.onPause()
        binding.rvRepos.removeOnItemTouchListener(onTouchListener)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(MyRepository(MyApiService.create()))
        ).get(MyViewModel::class.java)
        fetchProducts("*")
    }

    override fun setOnActivityTouchListener(listener: OnActivityTouchListener?) {
        this.touchListener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        touchListener?.getTouchCoordinates(ev)
        return super.dispatchTouchEvent(ev)
    }
}