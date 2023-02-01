package com.example.pokedex.ui.recycleView.pokemon

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * PaginationScrollListener is the abstract class that will be used to listen to the scroll events
 *
 * This class will be used to listen to the scroll events and will be used by the adapter to load
 * more items when the user scrolls to the end of the list.
 *
 * @param layoutManager The layout manager that will be used to get the information about the list
 */
abstract class PaginationScrollListener(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    /**
     * This method is called when the user scrolls the list. It will check if the user has scrolled
     * to the end of the list and if it has, it will call the loadMoreItems method.
     *
     * @param recyclerView The recycler view that will be used to get the information about the list
     * @param dx The distance that the user has scrolled horizontally
     * @param dy The distance that the user has scrolled vertically
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

        if (isLoading() && !isLastOffset()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    /**
     * This method will be used to load more items when the user scrolls to the end of the list
     */
    protected abstract fun loadMoreItems()

    /**
     * This method will be used to get the last offset of the list
     *
     * @return The last offset of the list
     */
    abstract fun getLastOffset(): Int

    /**
     * This method will be used to check if the last offset has been reached
     *
     * @return True if the last offset has been reached, false otherwise
     */
    abstract fun isLastOffset(): Boolean

    /**
     * This method will be used to check if the list is loading
     *
     * @return True if the list is loading, false otherwise
     */
    abstract fun isLoading(): Boolean
}