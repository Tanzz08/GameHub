
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemFavoriteBinding

import com.example.core.domain.model.ListItem

class FavoriteAdapter : ListAdapter<ListItem, FavoriteAdapter.ListViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((ListItem) -> Unit)? = null

    inner class ListViewHolder(private var binding: ItemFavoriteBinding ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListItem) {
            when (item) {
                is ListItem.FavoriteGame -> {
                    val game = item.game
                    Glide.with(itemView.context)
                        .load(game.backgroundImage)
                        .into(binding.imgItemPhoto)
                    binding.tvGameTitle.text = game.name
                    binding.tvGenre.text = game.genres
                    binding.tvRelease.text = game.released
                }
                is ListItem.NewReleaseGame -> {
                    val game = item.game
                    Glide.with(itemView.context)
                        .load(game.backgroundImage)
                        .into(binding.imgItemPhoto)
                    binding.tvGameTitle.text = game.name


                    binding.tvGenre.text = game.genres
                    binding.tvRelease.text = game.released
                }
            }
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
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListItem> =
            object : DiffUtil.ItemCallback<ListItem>() {
                override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                    return when {
                        oldItem is ListItem.FavoriteGame && newItem is ListItem.FavoriteGame ->
                            oldItem.game.gameId == newItem.game.gameId
                        oldItem is ListItem.NewReleaseGame && newItem is ListItem.NewReleaseGame ->
                            oldItem.game.gameId == newItem.game.gameId
                        else -> false
                    }
                }

                override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                    return oldItem == newItem
                }
            }
    }
}