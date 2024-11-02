package com.example.intermediatesubmission.ui.detail_story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.intermediatesubmission.R
import com.example.intermediatesubmission.databinding.ActivityDetailStoryBinding
import com.example.intermediatesubmission.util.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null
    private lateinit var detailStoryViewModel: DetailStoryViewModel

    private val binding get() = _binding!!

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        detailStoryViewModel = getDetailStoryViewModel()

        val id = intent.getStringExtra(EXTRA_ID)

        detailStoryViewModel.getDetailStory(id ?: "")

        observeData()
    }

    private fun getDetailStoryViewModel(): DetailStoryViewModel {
        val factory = ViewModelFactory.getInstance(this)
        return ViewModelProvider(this, factory)[DetailStoryViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeData() {
        detailStoryViewModel.state.observe(this) {
            with(binding) {
                val data = it.data
                setLoading(it.isLoading)
                setError(it.hasError)

                if (data == null) {
                    tvDetailName.text = getString(R.string.no_data)
                    return@with
                }
                tvDetailName.text = data.name
                tvDetailDescription.text = data.description
                tvDetailCreatedAt.text = data.createdAt
                tvDetailLongLat.text = getString(R.string.longitude_latitude, data.lon, data.lat)
                Glide.with(this@DetailStoryActivity)
                    .load(data.photoUrl).error(
                        AppCompatResources.getDrawable(
                            this@DetailStoryActivity,
                            R.drawable.baseline_broken_image_24
                        )
                    )
                    .into(ivDetailPhoto)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = isLoading
            tvDetailName.isVisible = !isLoading
            tvDetailDescription.isVisible = !isLoading
            tvDetailCreatedAt.isVisible = !isLoading
            tvDetailLongLat.isVisible = !isLoading
            ivDetailPhoto.isVisible = !isLoading
        }
    }

    private fun setError(isError: Boolean) {
        with(binding) {
            tvError.isVisible = isError
            tvDetailName.isVisible = !isError
            tvDetailDescription.isVisible = !isError
            tvDetailCreatedAt.isVisible = !isError
            tvDetailLongLat.isVisible = !isError
            ivDetailPhoto.isVisible = !isError
        }
    }
}