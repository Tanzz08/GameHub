package com.example.gamehub.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.source.Resource
import com.example.core.domain.model.GamesModel
import com.example.gamehub.search.databinding.ActivitySearchBinding
import com.example.gamehub.search.di.searchModule
import com.example.gamehub.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadKoinModules(searchModule)

        supportActionBar?.hide()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SearchAdapter()
        adapter.onItemClick = { selectedData ->
            startDetailActivity(selectedData.gameId!!.toInt(), selectedData)
        }

        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView(){
        adapter = SearchAdapter()
        adapter.onItemClick = { selectedData ->
            startDetailActivity(selectedData.gameId!!.toInt(), selectedData)
        }
        binding.recyclerViewResults.adapter = adapter
        binding.recyclerViewResults.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {

                    searchGames(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // Tidak langsung mencari saat mengetik, hanya setelah submit
            }
        })
    }

    private fun searchGames(query: String) {
        searchViewModel.searchGames(query).observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    val games = result.data
                    if (games.isNullOrEmpty()) {
                        showEmptyMessage(true)
                    } else {
                        showEmptyMessage(false)
                        adapter.submitList(games)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showEmptyMessage(true)
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startDetailActivity(gameId: Int, gamesModel: GamesModel?) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_DATA, gamesModel)
            putExtra(DetailActivity.EXTRA_GAME_ID, gameId)
        }
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}