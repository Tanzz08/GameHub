package com.example.gamehub.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.source.Resource
import com.example.core.domain.model.GamesModel
import com.example.core.ui.GameListAdapter
import com.example.core.ui.NewReleasesAdapter
import com.example.gamehub.R
import com.example.gamehub.databinding.FragmentHomeBinding
import com.example.gamehub.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val gameListAdapter: GameListAdapter by lazy { GameListAdapter() }
    private val newReleasesAdapter: NewReleasesAdapter by lazy { NewReleasesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            val uri = Uri.parse("gamehub://search")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        setupAdapters()
        observeNewReleasesGames()
        observePopularGames()

//        gameListAdapter.onItemClick = { selectedData ->
//            val intent = Intent(activity, DetailActivity::class.java)
//            intent.putExtra(DetailActivity.EXTRA_DATA, selectedData)
//            startActivity(intent)
//        }
//
//        newReleasesAdapter.onItemClick = { selectedData ->
//            val intent = Intent(activity, DetailActivity::class.java)
//            intent.putExtra(DetailActivity.EXTRA_DATA, selectedData)
//            startActivity(intent)
//        }

    }

    private fun setupAdapters() {
        newReleasesAdapter.onItemClick = { selectedData ->
            startDetailActivity(selectedData.gameId!!.toInt(), selectedData)
        }

        gameListAdapter.onItemClick = { selectedData ->
            startDetailActivity(selectedData.gameId!!.toInt(), selectedData)
        }

        with(binding.rvNewReleases) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newReleasesAdapter
        }

        with(binding.rvPopularGame) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = gameListAdapter
        }
    }

    private fun observeNewReleasesGames() {
        homeViewModel.newReleasesGame.observe(viewLifecycleOwner) { games ->
            handleNewReleasesState(games, newReleasesAdapter)
        }
    }

    private fun observePopularGames() {
        homeViewModel.games.observe(viewLifecycleOwner) { games ->
            handleGamesState(games, gameListAdapter)
        }
    }


    private fun handleGamesState(games: Resource<List<GamesModel>>?, adapter: GameListAdapter) {
        if (games != null) {
            when (games) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(games.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewError.tvError.text =
                        games.message ?: getString(R.string.something_wrong)
                }
            }
        }
    }

    private fun handleNewReleasesState(games: Resource<List<GamesModel>>?, adapter: NewReleasesAdapter) {
        if (games != null) {
            when (games) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(games.data) // Sesuaikan jika NewReleasesAdapter punya cara submitList yang berbeda
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewError.tvError.text =
                        games.message ?: getString(R.string.something_wrong)
                }
            }
        }
    }

    private fun startDetailActivity(gameId: Int, gamesModel: GamesModel?) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_DATA, gamesModel)
            putExtra(DetailActivity.EXTRA_GAME_ID, gameId)
        }
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}