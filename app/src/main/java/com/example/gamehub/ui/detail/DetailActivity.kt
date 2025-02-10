package com.example.gamehub.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.core.data.source.Resource
import com.example.core.domain.model.GamesModel
import com.example.gamehub.R
import com.example.gamehub.databinding.ActivityDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private val detailViewModel: DetailViewModel by viewModel()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val gameId = intent.getIntExtra(EXTRA_GAME_ID, -1)
        val detailGame = getParcelableExtra(intent, EXTRA_DATA, GamesModel::class.java)

        if (gameId != -1) {
            detailViewModel.getGameDetail(gameId)
        }

        observeGameDetail(detailGame)
    }

    private fun observeGameDetail(detailGame: GamesModel?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.gameDetail.collect { result ->
                    Log.d("DetailActivity", "ObserveGameDetail: Result = $result")
                    when (result) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Log.d("DetailActivity", "ObserveGameDetail: Loading")
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Log.d("DetailActivity", "ObserveGameDetail: Success, data = ${result.data}")

                            binding.tvGenre.text = result.data?.genres ?: "No Genres"
                            binding.tvReleaseDate.text = result.data?.released ?: "Unknown"

                            val description = result.data?.description ?: ""
                            binding.tvSummaryDesc.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)

                            binding.tvPlatform.text = result.data?.parentPlatforms ?: "No Platforms"

                            showDetailGame(detailGame, result.data?.name, result.data?.backgroundImage)
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Log.e("DetailActivity", "ObserveGameDetail: Error, message = ${result.message}")
                            Toast.makeText(
                                this@DetailActivity,
                                "Error: ${result.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }


    private fun showDetailGame(detailGame: GamesModel?, name: String?, image: String?) {
        detailGame?.let {
            binding.tvTitleDetail.text = name ?: it.name
            Glide.with(this@DetailActivity)
                .load(image ?: it.backgroundImage)
                .into(binding.imgCoverDetail)
            Glide.with(this@DetailActivity)
                .load(image ?: it.backgroundImage)
                .into(binding.imgHero)

            var statusFavorite = detailGame.isFavorite
            setStatusFavorite(statusFavorite)
            binding.fabFavorite.setOnClickListener {
                statusFavorite = !statusFavorite
                detailViewModel.setFavoriteGames(detailGame, statusFavorite)
                detailViewModel.setNewFavoriteGames(detailGame, statusFavorite)
                detailViewModel.updateFavoriteGame(detailGame, statusFavorite)
                setStatusFavorite(statusFavorite)
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_star_24))
        } else {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_star_border_24))
        }
    }


    companion object {
        const val EXTRA_GAME_ID = "extra_game_id"
        const val EXTRA_DATA = "extra_data"
    }
}