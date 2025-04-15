package com.hilmi.wartego.ui.auth.profile.address.add_address

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
import com.google.android.material.chip.Chip
import com.hilmi.wartego.R
import com.hilmi.wartego.databinding.FragmentAddAddressBinding
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddAddressFragment : Fragment() {

    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddAddressViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()

        binding.chipGroup.check(R.id.chip_home)
        binding.btnSaveLocation.setOnClickListener {
            checkAddressValidation()
        }
    }

    private fun checkAddressValidation() {
        with(binding) {
            val address = tfAddress.text.toString()
            val street = tfStreet.text.toString()
            val posCode = tfPostCode.text.toString()
            val apartment = tfAppartment.text.toString()
            val selectedChipId = chipGroup.checkedChipId
            val selectedChip = chipGroup.findViewById<Chip>(selectedChipId)
            val label = selectedChip.text.toString()

            if (address.isNotEmpty() && street.isNotEmpty() && posCode.isNotEmpty() && apartment.isNotEmpty() && label.isNotEmpty()) {
                addNewAddress(
                    Address(
                        address = address,
                        street = street,
                        postCode = posCode.toInt(),
                        apartment = apartment,
                        label = label
                    )
                )
            } else {
                Toast.makeText(context, "Please field doesn't empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewAddress(address: Address) {
        viewModel.addAddress(address)
    }

    private fun observerData() {
        viewModel.addOnSuccess.filterNotNull().onEach {
            when (it) {
                is Response.Error -> {
                    showLoading(false)
                    Toast.makeText(context, "Upload Address is failed", Toast.LENGTH_SHORT).show()
                }

                is Response.Loading -> {
                    showLoading(true)
                }

                is Response.Success -> {
                    showLoading(false)
                    Toast.makeText(context, "Upload Address is Successful", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(AddAddressFragmentDirections.actionAddAddressFragmentToAddressFragment())
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