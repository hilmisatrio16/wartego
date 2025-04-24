package com.hilmi.wartego.ui.cart

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hilmi.wartego.adapters.CartListAdapter
import com.hilmi.wartego.databinding.FragmentCartBinding
import com.hilmi.wartego.model.profile.CartEntity
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerDataCart()
        observerBtnEdit()
        binding.tvEdit.setOnClickListener {
            viewModel.isEdit()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observerBtnEdit() {

        viewModel.editable.onEach {

            val listDataEditble: List<CartEntity> = viewModel.dataCart.map { data ->
                data.copy(idEditable = it)
            }
            showRecycleView(listDataEditble)
            if (it) {
                binding.tvEdit.text = addUnderline("DONE")
                binding.tvEdit.setTextColor(Color.parseColor("#059C6A"))
            } else {
                binding.tvEdit.text = addUnderline("EDIT ITEMS")
                binding.tvEdit.setTextColor(Color.parseColor("#FF7622"))
            }
        }.launchIn(lifecycleScope)
    }

    private fun addUnderline(str: String): SpannableString {
        val mSpannableString = SpannableString(str)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        return mSpannableString
    }

    private fun observerDataCart() {
        viewModel.cart.onEach {
            when (it) {
                is Response.Error -> {

                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    showRecycleView(it.data)
                    viewModel.addDataCart(it.data)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showRecycleView(data: List<CartEntity>) {
        val cartAdapter = CartListAdapter()

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        cartAdapter.submitData(data as ArrayList<CartEntity>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}