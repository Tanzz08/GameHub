package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemPopularListBinding
import com.example.core.domain.model.GamesModel

class GameListAdapter : ListAdapter<GamesModel, GameListAdapter.ListViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((GamesModel) -> Unit)? = null

    inner class ListViewHolder(private var binding: ItemPopularListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GamesModel) {
            Glide.with(itemView.context)
                .load(data.backgroundImage)
                .into(binding.ivItemImage)
            binding.tvItemTitle.text = data.name
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            ItemPopularListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
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