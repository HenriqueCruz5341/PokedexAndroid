package com.example.pokedex.ui.recycleView.pageableitem

import com.example.pokedex.repository.api.model.PageableItemDto

interface OnPageableItemListener {
    fun onClick(region: PageableItemDto)
}