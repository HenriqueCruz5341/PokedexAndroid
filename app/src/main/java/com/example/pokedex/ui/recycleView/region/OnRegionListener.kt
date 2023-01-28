package com.example.pokedex.ui.recycleView.region

import com.example.pokedex.repository.api.model.PageableItemDto

interface OnRegionListener {
    fun onClick(region: PageableItemDto)
}