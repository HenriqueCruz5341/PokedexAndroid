package com.example.pokedex.ui.recycleView.pokemon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.LoadingLineBinding
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity

/**
 * ListPokemonAdapter is a class used to display a list of Pokemon in a RecyclerView
 *
 * This class is used to display a list of Pokemon in a RecyclerView. It is used in the PokemonFragment
 * and has methods to support an infinite scroll.
 *
 * @property pokemonList is a list of PokemonPageableEntity that is used to display the list of Pokemon
 * @property listener is an OnPokemonListener that is used to handle the click on a Pokemon
 * @property loading is an Int that is used to identify the loading line
 * @property spacing is an Int that is used to identify the spacing line
 * @property item is an Int that is used to identify the Pokemon line
 * @property isLoadingAdded is a Boolean that is used to know if the loading line is displayed
 */
class ListPokemonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var pokemonList: MutableList<PokemonPageableEntity> = mutableListOf()
    private lateinit var listener: OnPokemonListener

    private val loading: Int = -1
    private val spacing: Int = 0
    private val item: Int = 1

    private var isLoadingAdded: Boolean = false

    /**
     * This method is used to create a new ViewHolder for the RecyclerView, depending on the viewType
     * parameter. The viewType is determined by getItemViewType method.
     *
     * @param parent is a ViewGroup that is used to create the ViewHolder
     * @param viewType is an Int that is used to know which ViewHolder to create
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding = PokemonLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ListPokemonViewHolder(binding, listener)
        }else{
            val binding= LoadingLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    /**
     * This method is used to bind the data to the ViewHolder, depending on the position parameter.
     * The pokemonList is populated with pokemon and the loading line. The spacing line is used to
     * display a spacing between the Pokemon and the loading line. When is a pokemon, ListPokemonViewHolder
     * is used to bind the data to the view. When is the loading or spacing, LoadingViewHolder is
     * used to bind the data to the view, but if is the spacing, the bindVH method is called to hide
     * the loading.
     *
     * @param holder is a RecyclerView.ViewHolder that is used to bind the data
     * @param position is an Int that is used to know which data to bind
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        if(getItemViewType(position) == item) {
            val pokemonVH: ListPokemonViewHolder = holder as ListPokemonViewHolder
            pokemonVH.bindVH(pokemon)
        } else {
            val loadingVH: LoadingViewHolder = holder as LoadingViewHolder
            if (getItemViewType(position) == spacing) loadingVH.bindVH()
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.count()
    }

    /**
     * This method is used to know which ViewHolder to create, depending on the position parameter.
     * The pokemonList is populated with pokemon and the loading line. The spacing line is used to
     * display a spacing between the Pokemon and the loading line. When the pokemon list is populated
     * negative ids are used to identify the loading and spacing lines.
     *
     * @param position is an Int that indicates the position of the item in the list
     */
    override fun getItemViewType(position: Int): Int {
        return if (pokemonList[position].id > 0) item else pokemonList[position].id
    }

    /**
     * This method is used to add a list of Pokemon to the pokemonList. The list is added only if
     * the first pokemon id is greater than the last pokemon id in the pokemonList. This method is
     * used to avoid duplicates when the list is updated.
     *
     * @param pokemons is a List of PokemonPageableEntity that is used to add to the pokemonList
     */
    fun addAll(pokemons: List<PokemonPageableEntity>) {
        if (pokemonList.isNotEmpty() && pokemons[0].id < pokemonList[pokemonList.size-3].id) return

        for(pokemon in pokemons){
            add(pokemon)
        }
    }

    /**
     * This method is used to set a list of Pokemon to the pokemonList. In this case the pokemonList
     * is cleared and the new list is added.
     *
     * @param pokemons is a List of PokemonPageableEntity that is used to set to the pokemonList
     */
    fun setItems(pokemons: List<PokemonPageableEntity>) {
        val size = pokemonList.size
        pokemonList.clear()
        notifyItemRangeRemoved(0, size)
        for (pokemon in pokemons) {
            add(pokemon)
        }
    }

    /**
     * This method is used to add a Pokemon to the pokemonList. The Pokemon is added at the end of
     * the list and the notifyItemInserted method is called to notify the RecyclerView that the
     * item has been inserted.
     *
     * @param pokemon is a PokemonPageableEntity that is used to add to the pokemonList
     */
    private fun add(pokemon: PokemonPageableEntity) {
        pokemonList.add(pokemon)
        notifyItemInserted(pokemonList.size - 1)
    }

    /**
     * This method is used to add the loading line to the pokemonList. The loading line is added
     * only if the loading line is not already added. The spacing line is added before the loading
     * line to display a spacing between the Pokemon and the loading line.
     */
    fun addLoadingFooter() {
        if(!isLoadingAdded) {
            add(PokemonPageableEntity().apply { id = spacing; name = "Spacing"})
            add(PokemonPageableEntity().apply { id = loading; name = "Loading"})
            isLoadingAdded = true
        }
    }

    /**
     * This method is used to remove the loading line from the pokemonList. The loading line is
     * removed only if the loading line is already added. The spacing line is removed after the
     * loading line.
     */
    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            val positionLoading: Int = pokemonList.size - 1
            pokemonList.removeAt(positionLoading)
            notifyItemRemoved(positionLoading)

            val positionSpacing: Int = pokemonList.size - 1
            pokemonList.removeAt(positionSpacing)
            notifyItemRemoved(positionSpacing)
            isLoadingAdded = false
        }
    }

    /**
     * This method is used to set the listener to the RecyclerView. The listener is used to
     * communicate with the activity.
     *
     * @param pokemonListener is an OnPokemonListener that is used to set to the listener
     */
    fun setListener(pokemonListener: OnPokemonListener) {
        listener = pokemonListener
    }
}