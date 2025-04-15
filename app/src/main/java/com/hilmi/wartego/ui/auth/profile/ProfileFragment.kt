package com.hilmi.wartego.ui.auth.profile

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
import com.hilmi.wartego.databinding.FragmentProfileBinding
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()

        binding.personalInfo.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPersonalInfoFragment())
        }

        binding.address.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAddressFragment())
        }

        binding.logout.setOnClickListener {
            confirmToLogOut()
        }
    }

    private fun confirmToLogOut() {
        viewModel.logOut()
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAuthActivity())
        requireActivity().finish()
    }

    private fun observerData() {
        viewModel.user.onEach {
            when (it) {
                is Response.Error -> {
                    showLoading(false)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is Response.Loading -> {
                    showLoading(true)
                }

                is Response.Success -> {
                    showLoading(false)
                    with(binding) {
                        tvName.text = it.data.fullName
                        tvDesc.text = it.data.bio
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showLoading(isShow: Boolean) {
        binding.circularLoading.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}