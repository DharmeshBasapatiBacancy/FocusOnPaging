/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.focusonpaging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.focusonpaging.R
import com.example.focusonpaging.databinding.ProductsLoadStateFooterViewItemBinding

class ProductsLoadStateViewHolder(
    private val binding: ProductsLoadStateFooterViewItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {

        binding.progressBar.isVisible = loadState is LoadState.Loading

    }

    companion object {
        fun create(parent: ViewGroup): ProductsLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.products_load_state_footer_view_item, parent, false)
            val binding = ProductsLoadStateFooterViewItemBinding.bind(view)
            return ProductsLoadStateViewHolder(binding)
        }
    }
}
