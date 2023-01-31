package com.example.pokedex.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentHomeBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.ui.recycleView.pokemon.ListPokemonAdapter
import com.example.pokedex.ui.recycleView.pokemon.OnPokemonListener
import com.example.pokedex.ui.recycleView.pokemon.PaginationScrollListener
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Resources
import com.google.android.material.snackbar.Snackbar

/**
 * Home Fragment is the first fragment that the user sees when the app is opened. It shows a list of
 * pokemons, with a search bar to filter the list.
 *
 * The list is paginated, so when the user scrolls down, the next page is loaded automatically.
 * The search bar is used to filter the list by name or id. When the user types something in the search
 * bar and click on search button, the list is filtered. When the user deletes the text in the
 * search bar and click on search button, the list is restored to the original state.
 * When the user clicks on a pokemon, it navigates to the Pokemon Fragment and shows the details of
 * the pokemon.
 * The list is paginated using the PaginationScrollListener class.
 *
 * @see PaginationScrollListener
 * @see com.example.pokedex.ui.pokemon.PokemonFragment
 * @property homeViewModel the ViewModel of this fragment
 * @property binding the binding of this fragment
 * @property pokemonsAdapter the adapter of the recycler view
 * @property offsetStart the offset of the first page
 * @property isLoading true if the next page is loading, false otherwise
 * @property isFiltered true if the list is filtered, false otherwise
 * @property isLastOffset true if the last page is loaded, false otherwise
 * @property lastOffset the offset of the last page
 * @property currentOffset the offset of the current page
 * @property limit the limit of the page
 *
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private val binding get() = _binding!!
    private val pokemonsAdapter = ListPokemonAdapter()
    private val offsetStart: Int = 0
    private var isLoading: Boolean = true
    private var isFiltered: Boolean = false
    private var isLastOffset: Boolean = false
    private var lastOffset: Int = offsetStart
    private var currentOffset: Int = offsetStart
    private var limit: Int = 30

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        configureListeners()

        initRecyclerListPokemons()

        setObserver()

        return root
    }

    /**
     * This method configures the listeners of the search button.
     *
     * When the user clicks on the search button, if the search bar is not empty, the list is filtered
     * by the text in the search bar. If the search bar is empty and the list is filtered, the list is
     * restored to the original state.
     */
    private fun configureListeners() {
        binding.searchButton.setOnClickListener {
            if (binding.searchInput.text.isNotEmpty()) {
                isFiltered = true
                pokemonsAdapter.removeLoadingFooter()
                homeViewModel.searchPokemons(binding.searchInput.text.toString())
            } else if (isFiltered) {
                isFiltered = false

                pokemonsAdapter.setItems(homeViewModel.getPokemonList.value!!)

                if (currentOffset != lastOffset) {
                    isLoading = false
                    pokemonsAdapter.addLoadingFooter()
                    currentOffset += limit
                    loadNextPage()
                } else {
                    isLastOffset = true
                }
            }
        }
    }

    /**
     * This method init the recycler view of the list of pokemons.
     *
     * The recycler view is a grid layout with 3 columns. The adapter of the recycler view is the
     * ListPokemonAdapter. A listener is set to the adapter to listen when the user clicks on a
     * pokemon. When the user clicks on a pokemon, it navigates to the Pokemon Fragment.
     * The recycler view is paginated using the PaginationScrollListener class.
     * @see PaginationScrollListener
     */
    private fun initRecyclerListPokemons() {
        binding.recyclerListPokemons.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListPokemons.adapter = pokemonsAdapter

        val listener = object : OnPokemonListener {
            override fun onClick(pokemon: PokemonPageableEntity) {
                val action = HomeFragmentDirections.actionNavigationHomeToPokemonFragment(pokemon.id)
                findNavController().navigate(action)
            }
        }
        pokemonsAdapter.setListener(listener)

        loadNextPage()

        binding.recyclerListPokemons.addOnScrollListener(object : PaginationScrollListener(binding.recyclerListPokemons.layoutManager as GridLayoutManager) {
            override fun loadMoreItems() {
                if(!isFiltered) {
                    isLoading = false
                    currentOffset += limit

                    Handler(Looper.myLooper()!!).postDelayed({
                        loadNextPage()
                    }, 1000)
                }
            }

            override fun getLastOffset(): Int {
                return lastOffset
            }

            override fun isLastOffset(): Boolean {
                return isLastOffset
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
    }

    /**
     * This method loads the next page of the list of pokemons.
     *
     * The method calls the loadPokemons method of the HomeViewModel. The method is called when the
     * user scrolls down and the next page is loaded automatically. Or when the user clicks on the
     * search button to remove filter.
     */
    fun loadNextPage() {
        homeViewModel.loadPokemons(currentOffset, limit)
    }

    /**
     * This method sets the observers of the HomeViewModel.
     *
     * The observer of the getPokemonList is called when the list of pokemons is loaded, then we add
     * the pokemons to the adapter of the recycler view. If the list is filtered nothing is done,
     * else the loading footer is removed. if it's not loading, and if the user is not in the
     * last page, we add a loading footer to the adapter of the recycler view.
     * The observer of the getFilteredPokemonList is called when the list of pokemons is filtered.
     * And we set the items of the adapter of the recycler view with the filtered list. If the search
     * bar is empty and the list is filtered, we restore the list to the original state.
     * The observer of the getStatusMessage is called when the status message is changed. If the
     * status message is not a success message, we show a toast with the status message.
     */
    private fun setObserver() {
        homeViewModel.getPokemonList.observe(viewLifecycleOwner) {
            if (isFiltered) return@observe
            if (!isLoading) pokemonsAdapter.removeLoadingFooter()

            lastOffset = it[0].count / limit * limit

            pokemonsAdapter.addAll(it.subList(it.size - limit, it.size))

            if (currentOffset != lastOffset) {
                isLoading = true
                pokemonsAdapter.addLoadingFooter()
            } else {
                isLastOffset = true
            }
        }
        homeViewModel.getFilteredPokemonList.observe(viewLifecycleOwner) {
            if (binding.searchInput.text.isEmpty()) {
                isFiltered = false
                loadNextPage()
            } else {
                isFiltered = true
                pokemonsAdapter.setItems(it)
            }
        }
        homeViewModel.getStatusMessage.observe(viewLifecycleOwner) {
            if (it.code != Constants.DB_MSGS.SUCCESS && it.code != Constants.API_MSGS.SUCCESS) {
                showSnackBar(
                    resources.getString(Resources.getErrorMessageByStatusMessage(it))
                        .replace("{{id}}", it.item.toString())
                )
            }
        }
    }

    /**
     * This method shows a SnackBar with the text passed as parameter.
     *
     * @param text The text to show in the SnackBar.
     */
    private fun showSnackBar(text: String) {
        val snack = Snackbar.make(binding.homeFragment, text, Snackbar.LENGTH_SHORT)
        snack.setBackgroundTint(resources.getColor(R.color.red, null))
        snack.setTextColor(Color.WHITE)
        snack.setTextMaxLines(2)

        snack.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}