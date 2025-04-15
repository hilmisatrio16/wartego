package com.hilmi.wartego.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hilmi.wartego.R
import com.hilmi.wartego.databinding.ItemAddressBinding
import com.hilmi.wartego.model.profile.Address

class AddressListAdapter :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    private var diffCallbackUser = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(
            oldItem: Address,
            newItem: Address
        ): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(
            oldItem: Address,
            newItem: Address
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private var differ = AsyncListDiffer(this, diffCallbackUser)

    fun submitData(valueList: ArrayList<Address>) {
        differ.submitList(valueList)
    }

    class ViewHolder(private var binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Address) {
            with(binding) {
                tvLabel.text = data.label
                tvAddress.text = data.address
                when (data.label) {
                    "Home" -> {
                        ivIconLabel.setImageResource(R.drawable.home_24px)
                    }

                    "Work" -> {
                        ivIconLabel.setImageResource(R.drawable.work_24px)
                    }

                    else -> {
                        ivIconLabel.setImageResource(R.drawable.other_houses_24px)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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