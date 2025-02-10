package com.example.gamehub.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.GamesModel
import com.example.gamehub.search.databinding.ItemSearchBinding

class SearchAdapter: ListAdapter<GamesModel, SearchAdapter.ListViewModel>(DIFF_CALLBACK) {
    var onItemClick: ((GamesModel) -> Unit)? = null

    inner class ListViewModel(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (game: GamesModel) {
            binding.tvGameTitle.text = game.name
            binding.tvGenre.text = game.parentPlatforms
            binding.tvRelease.text = game.released
            Glide.with(itemView.context)
                .load(game.backgroundImage)
                .into(binding.imgItemPhoto)

        }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewModel {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewModel(binding)
    }

    override fun onBindViewHolder(holder: ListViewModel, position: Int) {
        val game = getItem(position)
        holder.bind(game)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GamesModel>() {
            override fun areItemsTheSame(oldItem: GamesModel, newItem: GamesModel): Boolean {
                return oldItem.gameId == newItem.gameId
            }

            override fun areContentsTheSame(oldItem: GamesModel, newItem: GamesModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}