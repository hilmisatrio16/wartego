package com.hilmi.wartego.ui.auth.profile.address.myaddress

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.hilmi.wartego.R
import com.hilmi.wartego.adapters.AddressListAdapter
import com.hilmi.wartego.databinding.FragmentAddAddressBinding
import com.hilmi.wartego.databinding.FragmentAddressBinding
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.response.Response
import com.hilmi.wartego.ui.auth.profile.address.add_address.AddAddressFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddressViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddAddress.setOnClickListener {
            findNavController().navigate(AddressFragmentDirections.actionAddressFragmentToAddAddressFragment())
        }

        observerData()
    }

    private fun observerData() {
        viewModel.address.onEach {
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
                    showRecycleView(it.data)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showRecycleView(data: List<Address>) {
        val listAdapter = AddressListAdapter()

        with(binding) {
            rvAddress.layoutManager = LinearLayoutManager(context)
            rvAddress.adapter = listAdapter
        }

        listAdapter.submitData(data as ArrayList<Address>)
    }

    private fun showLoading(isShow: Boolean) {
        binding.circularLoading.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}