package com.udacity.asteroidradar.main

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AdtroidListItemBinding
import java.text.FieldPosition

class AstroidListAdapter(val clickListener: OnClickListener): ListAdapter<Asteroid, AstroidListAdapter.ViewHolder>(DiffClass()) {
    class ViewHolder(private var binding:AdtroidListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(astreoid: Asteroid?) {
            binding.astreoid=astreoid
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent:ViewGroup):ViewHolder {
                val inflater=LayoutInflater.from(parent.context)
                val binding=DataBindingUtil.inflate<AdtroidListItemBinding>(inflater, R.layout.adtroid_list_item,parent,false)
                return ViewHolder(binding)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
    override fun onBindViewHolder(holder:ViewHolder,position: Int) {
        val asteroid = getItem(position)

        //view.setOnClickListener
        holder.itemView.setOnClickListener {

            clickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }
}
class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
class DiffClass : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {

        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {

        return oldItem == newItem
    }

}
