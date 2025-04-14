package com.hilmi.wartego.ui.auth.register

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
import com.hilmi.wartego.databinding.FragmentRegisterBinding
import com.hilmi.wartego.model.response.Response
import com.hilmi.wartego.ui.auth.login.LoginFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerUser.filterNotNull().onEach {
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
                    Toast.makeText(context, "Register Successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToDashboardActivity())
                }
            }
        }.launchIn(lifecycleScope)

        binding.btnSignup.setOnClickListener {
            registerUser()
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun confirmPassword(): Boolean {
        return binding.tfPassword.text.toString() == binding.tfConfirmPassword.text.toString()
    }

    private fun registerUser() {
        val email = binding.tfEmail.text.toString()
        val password = binding.tfPassword.text.toString()
        val fullName = binding.tfName.text.toString()
        val validate = validationLogin()
        if (validate) {
            if(confirmPassword()){
                viewModel.register(email, password, fullName)
            }else{
                Toast.makeText(context, "Password not match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please fill these field!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding.circularLoading.isVisible = isShow
    }

    private fun validationLogin(): Boolean {
        return binding.tfEmail.text.isNotEmpty() && binding.tfPassword.text.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}