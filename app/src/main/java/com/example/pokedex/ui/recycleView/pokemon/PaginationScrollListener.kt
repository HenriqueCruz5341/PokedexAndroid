package com.example.pokedex.ui.recycleView.pokemon

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

//        Log.d("Pagination", "================ PaginationScrollListener =================")
//        Log.d("Pagination", "visibleItemCount: $visibleItemCount")
//        Log.d("Pagination", "totalItemCount: $totalItemCount")
//        Log.d("Pagination", "firstVisibleItemPosition: $firstVisibleItemPosition")
//        Log.d("Pagination", "isLoading: ${isLoading()}")
//        Log.d("Pagination", "isLastOffset: ${isLastOffset()}")


        if (!isLoading() && !isLastOffset()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
//                Log.d("Pagination", "loadMoreItems() <----------------")
                loadMoreItems()
            }
        }
//        Log.d("Pagination", "===========================================================")
    }

    protected abstract fun loadMoreItems()
    abstract fun getLastOffset(): Int
    abstract fun isLastOffset(): Boolean
    abstract fun isLoading(): Boolean
}