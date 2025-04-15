package com.hilmi.wartego.ui.auth.profile.info.edit_profile

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
import com.hilmi.wartego.databinding.FragmentEditProfileBinding
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.request.UpdateUser
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()

        binding.btnSave.setOnClickListener {
            checkField()
        }

        observerUpdateUser()
    }

    private fun observerUpdateUser() {
        viewModel.editOnSuccess.filterNotNull().onEach {
            if (it) {
                Toast.makeText(context, "Updated is Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToPersonalInfoFragment())
            } else {
                Toast.makeText(context, "Updated is Failed", Toast.LENGTH_SHORT).show()
            }
            showLoading(false)
        }.launchIn(lifecycleScope)
    }

    private fun checkField() {
        val tfName = binding.tfName.text.toString()
        val tfPhone = binding.tfPhoneNumber.text.toString()
        val tfBio = binding.tfBio.text.toString()

        if (tfName.isNotEmpty() && tfPhone.isNotEmpty() && tfBio.isNotEmpty()) {
            showLoading(true)
            updateUser(UpdateUser(tfName, tfPhone, tfBio))
        } else {
            Toast.makeText(context, "Please field doesn't empty!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUser(user: UpdateUser) {
        viewModel.updateUser(user)
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
                    putData(it.data)
                }
            }
        }.launchIn(lifecycleScope)

    }

    private fun putData(data: User) {
        with(binding) {
            tfName.setText(data.fullName)
            tfEmail.setText(data.email)
            tfPhoneNumber.setText(data.phoneNumber)
            tfBio.setText(data.bio)
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding.circularLoading.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}