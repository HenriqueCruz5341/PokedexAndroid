package com.example.pokedex.ui.recycleView.evolution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.EvolutionLineBinding
import com.example.pokedex.databinding.NextLineBinding
import com.example.pokedex.repository.database.model.EvolutionEntity

/**
 * ListEvolutionAdapter is a class used to display a list of Evolutions in a RecyclerView
 *
 * This class is used to display a list of Evolutions in a RecyclerView. It is used in the PokemonFragment
 * and has methods to support to dynamically display Evolutions interspersed with next arrows.
 *
 * @property evolutionList is a list of EvolutionEntity that is used to display the list of Evolutions
 * @property listener is an OnEvolutionListener that is used to handle the click on an Evolution
 * @property item is an Int that is used to identify the Evolution line
 * @property next is an Int that is used to identify the next line
 */
class ListEvolutionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var evolutionList: List<EvolutionEntity> = listOf()
    private lateinit var listener: OnEvolutionListener

    private val item: Int = 0
    private val next: Int = 1

    /**
     * This method is used to create a new ViewHolder for the RecyclerView, depending on the viewType
     * parameter. The viewType is determined by getItemViewType method.
     *
     * @param parent is a ViewGroup that is used to create the ViewHolder
     * @param viewType is an Int that is used to know which ViewHolder to create
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == item) {
            val binding =
                EvolutionLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ListEvolutionViewHolder(binding, listener)
        } else {
            val binding =
                NextLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NextViewHolder(binding)
        }
    }

    /**
     * This method is used to bind the data to the ViewHolder, depending on the position parameter.
     * The evolutionList is populated with evolution and the next line. When is a evolution,
     * ListEvolutionViewHolder is used to bind the data to the view. When is the next arrow,
     * is not necessary to bind the data.
     *
     * @param holder is a RecyclerView.ViewHolder that is used to bind the data
     * @param position is an Int that is used to know which data to bind
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val evolution = evolutionList[position]

        if (getItemViewType(position) == item) {
            val evolutionVH: ListEvolutionViewHolder = holder as ListEvolutionViewHolder
            evolutionVH.bindVH(evolution)
        }
    }

    /**
     * This method is used to know which ViewHolder to create, depending on the position parameter.
     * The evolutionList is populated with evolution and the next line. When the evolution list is
     * populated ids equals to -1 are used to identify the next lines.
     *
     * @param position is an Int that indicates the position of the item in the list
     */
    override fun getItemViewType(position: Int): Int {
        return if (evolutionList[position].id == -1) next
        else item
    }

    override fun getItemCount(): Int {
        return evolutionList.count()
    }

    /**
     * This method is used to update the evolutionList with a new list of EvolutionEntity
     *
     * @param list is a List of EvolutionEntity that is used to update the evolutionList
     */
    fun updateEvolutionList(list: List<EvolutionEntity>) {
        evolutionList = list
        notifyDataSetChanged()
    }

    /**
     * This method is used to set the listener to the adapter
     *
     * @param evolutionListener is an OnEvolutionListener that is used to handle the click on an Evolution
     */
    fun setListener(evolutionListener: OnEvolutionListener) {
        listener = evolutionListener
    }
}