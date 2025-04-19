package com.hilmi.wartego.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hilmi.wartego.R
import com.hilmi.wartego.databinding.ItemAddressBinding
import com.hilmi.wartego.databinding.ItemCartBinding
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.profile.Cart

class CartListAdapter :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    private var diffCallbackUser = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(
            oldItem: Cart,
            newItem: Cart
        ): Boolean {
            return oldItem.idProduct == newItem.idProduct
        }

        override fun areContentsTheSame(
            oldItem: Cart,
            newItem: Cart
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallbackUser)

    fun submitData(valueList: ArrayList<Cart>) {
        differ.submitList(valueList)
    }

    class ViewHolder(private var binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: Cart) {
            with(binding) {
                tvNameFood.text = data.nameProduct
                tvPrice.text = data.price
                tvQuantity.text = data.quantity.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]

        if (data != null) {
            holder.bind(data)
        }
    }
}