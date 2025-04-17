package com.hilmi.wartego.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hilmi.wartego.databinding.ItemRestaurantBinding
import com.hilmi.wartego.model.product.Restaurant

class RestaurantListAdapter(var itemClickRestaurant: (Restaurant)->Unit?) :
    RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>() {

    private var diffCallbackUser = object : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallbackUser)

    fun submitData(valueList: ArrayList<Restaurant>) {
        differ.submitList(valueList)
    }

    class ViewHolder(private var binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Restaurant) {
            with(binding){
                tvRestaurant.text = data.nameRestaurant
                tvMenu.text = data.category.joinToString(" - ") { it }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            itemClickRestaurant.invoke(data)
        }
    }
}