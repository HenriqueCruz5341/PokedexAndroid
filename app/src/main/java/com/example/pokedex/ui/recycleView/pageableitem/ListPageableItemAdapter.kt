package com.example.pokedex.ui.recycleView.pageableitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.RegionLineBinding
import com.example.pokedex.repository.api.model.PageableItemDto

class ListPageableItemAdapter : RecyclerView.Adapter<ListPageableItemViewHolder>() {

    private var regionList: List<PageableItemDto> = listOf()
    private lateinit var listener: OnPageableItemListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPageableItemViewHolder {
        val item = RegionLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ListPageableItemViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: ListPageableItemViewHolder, position: Int) {
        holder.bindVH(regionList[position])
    }

    override fun getItemCount(): Int {
        return regionList.count()
    }

    fun updateRegionList(list: List<PageableItemDto>) {
        regionList = list
        notifyDataSetChanged()
    }

    fun setListener(regionListener: OnPageableItemListener) {
        listener = regionListener
    }
}