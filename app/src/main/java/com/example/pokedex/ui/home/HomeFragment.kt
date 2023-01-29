package com.example.pokedex.ui.home

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
import com.example.pokedex.databinding.FragmentHomeBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.ui.recycleView.pokemon.ListPokemonAdapter
import com.example.pokedex.ui.recycleView.pokemon.OnPokemonListener
import com.example.pokedex.ui.recycleView.pokemon.PaginationScrollListener

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

    fun loadNextPage() {
        homeViewModel.loadPokemons(currentOffset, limit)
    }

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}