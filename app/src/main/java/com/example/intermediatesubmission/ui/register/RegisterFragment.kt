package com.example.intermediatesubmission.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.intermediatesubmission.R
import com.example.intermediatesubmission.data.model.UserModel
import com.example.intermediatesubmission.data.response.LoginResponse
import com.example.intermediatesubmission.data.retrofit.ApiConfig
import com.example.intermediatesubmission.data.retrofit.ApiService
import com.example.intermediatesubmission.databinding.FragmentRegisterBinding
import retrofit2.Callback

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiConfig.getApiService(requireContext())
        with(binding) {
            validateButton()

            registerButton.setOnClickListener {
                registerUser()
            }

            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateButton() {
        with(binding) {
            edRegisterPassword.errorText.observe(viewLifecycleOwner) {
                registerButton.isEnabled =
                    edRegisterEmail.errorText.value.isNullOrEmpty() && it.isNullOrEmpty()
            }

            edRegisterEmail.errorText.observe(viewLifecycleOwner) {
                registerButton.isEnabled =
                    edRegisterPassword.errorText.value.isNullOrEmpty() && it.isNullOrEmpty()
            }
        }
    }

    private fun registerUser() {
        // Usage of UserModel to make it more readable
        val userModel = UserModel(
            binding.edRegisterName.text.toString(),
            binding.edRegisterEmail.text.toString(),
            binding.edRegisterPassword.text.toString()
        )

        if (!userModel.isValid()) {
            Toast.makeText(context, R.string.field_must_be_filled, Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterFragment", "UserModel: $userModel")

        val client = apiService.register(
            userModel.name,
            userModel.email,
            userModel.password
        )

        binding.registerButton.isEnabled = false

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("RegisterFragment", "LoginResponse: $loginResponse")
                    Toast.makeText(context, R.string.register_success, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RegisterFragment", "Error: $errorBody")
                    Toast.makeText(context, R.string.register_failed, Toast.LENGTH_SHORT).show()
                }

                binding.registerButton.isEnabled = true
            }

            override fun onFailure(
                call: retrofit2.Call<LoginResponse>,
                t: Throwable
            ) {
                t.printStackTrace()
                Toast.makeText(context, R.string.register_failed, Toast.LENGTH_SHORT).show()
                binding.registerButton.isEnabled = true
            }
        })
    }
}