package com.dicoding.storyapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.ItemListStoryBinding
import com.dicoding.storyapp.model.ListStoryItem

class ListStoryAdapter(private val listStory: List<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ListStoryHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ListStoryHolder(val binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryHolder {
        val binding = ItemListStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ListStoryHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryHolder, position: Int) {
        with (holder) {
            with (listStory[position]) {
                binding.tvItemName.text = name
                binding.tvItemUsername.text = id
                binding.tvItemDescription.text = description

                Glide.with(holder.itemView)
                    .load(photoUrl)
                    .into(binding.imgItemStory)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClick(listStory[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = listStory.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClick(storyItem: ListStoryItem) {

        }
    }
}