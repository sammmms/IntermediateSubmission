package com.example.intermediatesubmission.ui.add_story

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmission.data.response.DetailStoryResponse
import com.example.intermediatesubmission.data.retrofit.ApiConfig
import com.example.intermediatesubmission.data.retrofit.ApiService
import com.example.intermediatesubmission.databinding.ActivityAddStoryBinding
import com.example.intermediatesubmission.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoryBinding? = null
    private lateinit var apiService: ApiService

    private val binding get() = _binding!!

    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var viewModel: AddStoryViewModel

    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setGalleryLauncher()
        setCameraLauncher()

        viewModel = ViewModelProvider(this)[AddStoryViewModel::class.java]
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        apiService = ApiConfig.getApiService(this)

        with(binding) {
            btnCamera.setOnClickListener {
                val file: File = File.createTempFile("temp", ".jpg", cacheDir)
                photoUri = FileProvider.getUriForFile(
                    this@AddStoryActivity,
                    "$packageName.fileprovider",
                    file
                )
                cameraLauncher.launch(photoUri!!)
            }

            btnGallery.setOnClickListener {
                galleryLauncher.launch("image/*")
            }

            buttonAdd.setOnClickListener {
                createStory()
            }
        }

        viewModel.state.observe(this) {
            setLoading(it.isLoading)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun createStory() {
        viewModel.isLoading()
        if (photoUri == null) {
            return
        }
        val file = uriToFile(photoUri!!, this)
        Log.d(
            "AddStoryActivity",
            "createStory: ${file.absolutePath}"
        )
        val requestFile = file.asRequestBody("image/jpeg".toMediaType())
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        val description =
            binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())

        val client = apiService.postStory(description = description, photo = body)

        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    showToast("Story created")
                    viewModel.initialState()
                    finish()
                } else {
                    showToast("Failed to create story")
                    viewModel.hasError()
                }

            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e("AddStoryActivity", "onFailure: ${t.message}")
                showToast("Failed to create story")
                viewModel.hasError()
            }
        })
    }

    private fun setGalleryLauncher() {
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            photoUri = uri
            binding.ivAddPhoto.setImageURI(uri)
        }
    }

    private fun setCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    binding.ivAddPhoto.setImageURI(photoUri)
                } else {
                    photoUri = null
                }
            }
    }

    private fun setLoading(isLoading: Boolean) {
        with(binding) {
            btnCamera.isEnabled = !isLoading
            btnGallery.isEnabled = !isLoading
            buttonAdd.isEnabled = !isLoading
            edAddDescription.isEnabled = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}