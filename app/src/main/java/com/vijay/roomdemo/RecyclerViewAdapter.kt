package com.vijay.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vijay.roomdemo.databinding.ListDataBinding
import com.vijay.roomdemo.db.Subscriber
import com.vijay.roomdemo.generated.callback.OnClickListener

class RecyclerViewAdapter(
    private val subscriberslist: List<Subscriber>,
    private val clickListener: (Subscriber) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ListDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_data, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subscriberslist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscriberslist[position], clickListener)
    }

}

class MyViewHolder(val binding: ListDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        binding.listItemLayout.setOnClickListener {
            clickListener(subscriber)
        }
    }
}