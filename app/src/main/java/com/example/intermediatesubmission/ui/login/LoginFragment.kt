package com.example.intermediatesubmission.ui.login

import android.content.Intent
import android.os.Bundle
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
import com.example.intermediatesubmission.databinding.FragmentLoginBinding
import com.example.intermediatesubmission.ui.home.HomeActivity
import com.example.intermediatesubmission.util.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private lateinit var apiService: ApiService
    private val sharedPreferences: SharedPreferences by lazy {
        SharedPreferences(requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiConfig.getApiService(requireContext())
        with(binding) {
            validateButton()

            loginButton.setOnClickListener {
                loginUser()
            }

            registerButton.setOnClickListener {
                findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateButton() {
        with(binding) {
            edLoginEmail.errorText.observe(viewLifecycleOwner) {
                loginButton.isEnabled =
                    (edLoginEmail.errorText.value?.isEmpty() ?: false) && it.isEmpty()
            }
            edLoginPassword.errorText.observe(viewLifecycleOwner) {
                loginButton.isEnabled =
                    (edLoginEmail.errorText.value?.isEmpty() ?: false) && it.isEmpty()
            }
        }
    }

    private fun loginUser() {
        val userModel = UserModel(
            email = binding.edLoginEmail.text.toString(),
            password = binding.edLoginPassword.text.toString()
        )

        if (userModel.email.isEmpty() || userModel.password.isEmpty()) {
            Toast.makeText(context, R.string.field_must_be_filled, Toast.LENGTH_SHORT).show()
            return
        }

        val client = apiService.login(userModel.email, userModel.password)
        binding.loginButton.isEnabled = false
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.message == "success") {
                        Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()
                        loginResponse.loginResult!!.token.let {
                            sharedPreferences.saveToken(it!!)
                        }
                        val intent = Intent(context, HomeActivity::class.java)
                        activity?.finish()
                        startActivity(intent)
                    } else {
                        loginResponse?.message?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        } ?: Toast.makeText(context, R.string.login_failed, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    response.errorBody()?.string()?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    } ?: Toast.makeText(context, R.string.login_failed, Toast.LENGTH_SHORT).show()
                }

                binding.loginButton.isEnabled = true
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, R.string.login_failed, Toast.LENGTH_SHORT).show()

                binding.loginButton.isEnabled = true
            }
        })

    }
}