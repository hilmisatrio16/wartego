package com.hilmi.wartego.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hilmi.wartego.databinding.ItemCartBinding
import com.hilmi.wartego.model.profile.CartEntity

class CartListAdapter() :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    private var diffCallbackUser = object : DiffUtil.ItemCallback<CartEntity>() {
        override fun areItemsTheSame(
            oldItem: CartEntity,
            newItem: CartEntity
        ): Boolean {
            return oldItem.idProduct == newItem.idProduct
        }

        override fun areContentsTheSame(
            oldItem: CartEntity,
            newItem: CartEntity
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallbackUser)

    fun submitData(valueList: ArrayList<CartEntity>) {
        differ.submitList(valueList)
    }

    class ViewHolder(private var binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: CartEntity) {
            with(binding) {
                tvNameFood.text = data.nameProduct
                tvPrice.text = data.price
                tvQuantity.text = data.quantity.toString()
                btnPlus.setOnClickListener {
                    val temp = tvQuantity.text.toString().toInt() + 1
                    tvQuantity.text = temp.toString()
                }
                btnDelete.isVisible = data.idEditable
                btnMin.setOnClickListener {
                    var temp = tvQuantity.text.toString().toInt()
                    if (temp >= 2) {
                        temp -= 1
                        tvQuantity.text = temp.toString()
                    }
                }
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