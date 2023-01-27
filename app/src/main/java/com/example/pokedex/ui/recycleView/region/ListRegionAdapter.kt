package com.example.pokedex.ui.recycleView.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.RegionLineBinding
import com.example.pokedex.databinding.TypeLineBinding
import com.example.pokedex.repository.database.model.TypeEntity

class ListRegionAdapter : RecyclerView.Adapter<ListRegionViewHolder>() {

    private var regionList: List<String> = listOf()
    private lateinit var listener: OnRegionListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRegionViewHolder {
        val item = RegionLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ListRegionViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: ListRegionViewHolder, position: Int) {
        holder.bindVH(regionList[position])
    }

    override fun getItemCount(): Int {
        return regionList.count()
    }

    fun updateRegionList(list: List<String>) {
        regionList = list
        notifyItemRangeChanged(0, list.size)
    }

    fun setListener(regionListener: OnRegionListener) {
        listener = regionListener
    }
}