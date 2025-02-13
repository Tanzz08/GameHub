package com.example.favorite.ui

import FavoriteAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.model.GamesModel
import com.example.core.domain.model.ListItem
import com.example.favorite.di.favoriteModule
import com.example.favorite.databinding.FragmentFavoriteBinding
import com.example.gamehub.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


class FavoriteFragment : Fragment() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var combinedAdapter: FavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(favoriteModule)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        combinedAdapter = FavoriteAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            binding.rvFavorite.layoutManager = LinearLayoutManager(context)
            binding.rvFavorite.adapter = combinedAdapter

            binding.btnSearch.setOnClickListener {
                val uri = Uri.parse("gamehub://search")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

            combinedAdapter?.onItemClick = { listItem ->
                when (listItem) {
                    is ListItem.FavoriteGame -> {
                        startDetailActivity(listItem.game.gameId!!.toInt(), listItem.game)
                    }

                    is ListItem.NewReleaseGame -> {
                        startDetailActivity(listItem.game.gameId!!.toInt(), listItem.game)
                    }
                }
            }

            favoriteViewModel.combinedListItems.observe(viewLifecycleOwner) { combinedListItems ->
                combinedAdapter?.submitList(combinedListItems)
                binding.viewEmpty.root.visibility =
                    if (combinedListItems.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        combinedAdapter?.onItemClick = null
        binding?.rvFavorite?.adapter = null
        combinedAdapter = null
        _binding = null
    }


    private fun startDetailActivity(gameId: Int, gamesModel: GamesModel?) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_DATA, gamesModel)
            putExtra(DetailActivity.EXTRA_GAME_ID, gameId)
        }
        startActivity(intent)
    }


}