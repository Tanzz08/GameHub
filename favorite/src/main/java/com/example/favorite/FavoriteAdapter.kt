package com.example.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.GamesModel
import com.example.favorite.databinding.ItemFavoriteBinding

class FavoriteAdapter : ListAdapter<GamesModel, FavoriteAdapter.ListViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((GamesModel) -> Unit)? = null

    inner class ListViewHolder(private var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GamesModel) {
            Glide.with(itemView.context)
                .load(data.backgroundImage)
                .into(binding.imgItemPhoto)
            binding.tvGameTitle.text = data.name


            binding.tvGenre.text = data.genres
            binding.tvRelease.text = data.released
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GamesModel> =
            object : DiffUtil.ItemCallback<GamesModel>() {
                override fun areItemsTheSame(oldItem: GamesModel, newItem: GamesModel): Boolean {
                    return oldItem.gameId == newItem.gameId
                }

                override fun areContentsTheSame(oldItem: GamesModel, newItem: GamesModel): Boolean {
                    return oldItem == newItem
                }
            }
    }

}