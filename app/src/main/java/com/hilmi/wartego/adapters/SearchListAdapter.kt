package com.hilmi.wartego.adapters

import com.hilmi.wartego.databinding.ItemRestaurantBinding
import com.hilmi.wartego.model.product.Product
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hilmi.wartego.databinding.ItemPopularBinding
import com.hilmi.wartego.databinding.ItemSearchviewBinding

class SearchListAdapter(var itemClickFood: (Product) -> Unit?) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    private var diffCallbackUser = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallbackUser)

    fun submitData(valueList: ArrayList<Product>) {
        differ.submitList(valueList)
    }

    class ViewHolder(private var binding: ItemSearchviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            with(binding) {
                tvName.text = data.nameProduct
                tvPrice.text = data.price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSearchviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        holder.itemView.setOnClickListener {
            itemClickFood.invoke(data)
        }
    }
}