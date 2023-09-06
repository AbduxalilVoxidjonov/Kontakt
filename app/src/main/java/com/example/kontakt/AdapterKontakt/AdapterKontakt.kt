package com.example.contactapp.AdapterKontakt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.models.Kontakt
import com.example.kontakt.databinding.TaskItemBinding

class MyDiff(): DiffUtil.ItemCallback<Kontakt>(){
    override fun areItemsTheSame(oldItem: Kontakt, newItem: Kontakt): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Kontakt, newItem: Kontakt): Boolean {
        return oldItem == newItem
    }

}

class AdapterKontakt(): ListAdapter<Kontakt,AdapterKontakt.Vh>(MyDiff()) {
    inner class Vh(val binding: TaskItemBinding):RecyclerView.ViewHolder(binding.root){
        fun find(kontakt: Kontakt){
            binding.taskName.text=kontakt.name
            binding.taskDesc.text=kontakt.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(TaskItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.find(getItem(position))
    }

}