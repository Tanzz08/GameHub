package com.example.favorite

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favorite.databinding.ActivityFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            loadKoinModules(favoriteModule)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        supportActionBar?.hide()

        getFavoriteGames()

    }

    private fun getFavoriteGames(){
        val favoriteAdapter = FavoriteAdapter()

        favoriteViewModel.favoriteGames.observe(this) { dataGames ->
            favoriteAdapter.submitList(dataGames)
            binding.viewEmpty.root.visibility =
                if (dataGames.isNotEmpty()) View.GONE else View.VISIBLE
        }

        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }
}