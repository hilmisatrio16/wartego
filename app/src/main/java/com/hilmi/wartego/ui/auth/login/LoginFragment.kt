package com.hilmi.wartego.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hilmi.wartego.R
import com.hilmi.wartego.databinding.FragmentLoginBinding
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginUser.filterNotNull().onEach {
            when (it) {
                is Response.Error -> {
                    showLoading(false)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                Response.Loading -> {
                    showLoading(true)
                }

                is Response.Success -> {
                    showLoading(false)
                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardActivity())
                }
            }
        }.launchIn(lifecycleScope)

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding.circularLoading.isVisible = isShow
    }

    private fun loginUser() {
        val email = binding.tfEmail.text.toString()
        val password = binding.tfPassword.text.toString()
        val validate = validationLogin()
        if (validate) {
            viewModel.login(email, password)
        } else {
            Toast.makeText(context, "Please fill these field!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validationLogin(): Boolean {
        return binding.tfEmail.text.isNotEmpty() && binding.tfPassword.text.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}