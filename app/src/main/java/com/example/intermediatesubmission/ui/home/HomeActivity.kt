package com.example.intermediatesubmission.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intermediatesubmission.R
import com.example.intermediatesubmission.databinding.ActivityHomeBinding
import com.example.intermediatesubmission.ui.MainActivity
import com.example.intermediatesubmission.ui.add_story.AddStoryActivity
import com.example.intermediatesubmission.util.SharedPreferences
import com.example.intermediatesubmission.util.StoryListAdapter
import com.example.intermediatesubmission.util.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val sharedPreferences: SharedPreferences by lazy {
        SharedPreferences(this)
    }
    private lateinit var homeViewModel: HomeViewModel

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        homeViewModel = getHomeViewModel()
        supportActionBar?.title = "Story"

        with(binding) {
            val adapter = StoryListAdapter()
            val rv = rvStory
            rv.layoutManager = LinearLayoutManager(this@HomeActivity)
            rv.adapter = adapter

            homeViewModel.state.observe(this@HomeActivity) {
                setLoading(it.isLoading)
                setError(it.hasError)
                if (it.data.isNullOrEmpty()) {
                    tvNoData.isVisible = true
                } else {
                    adapter.setStories(it.data!!)
                    tvNoData.isVisible = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val intent = Intent(this, MainActivity::class.java)
                sharedPreferences.clear()
                finish()
                startActivity(intent)
            }

            R.id.add_story -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
            }

            R.id.action_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getListStory()
    }

    private fun getHomeViewModel(): HomeViewModel {
        val factory = ViewModelFactory.getInstance(this)
        return ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setLoading(isLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = isLoading
            rvStory.isVisible = !isLoading
        }
    }

    private fun setError(isError: Boolean) {
        with(binding) {
            tvError.isVisible = isError
            rvStory.isVisible = !isError
        }
    }
}