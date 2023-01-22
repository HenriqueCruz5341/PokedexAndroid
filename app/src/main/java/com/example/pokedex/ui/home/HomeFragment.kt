package com.example.pokedex.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
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
    private var isLoading: Boolean = false
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

        initRecyclerListPokemons()

        setObserver()

        return root
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
                isLoading = true
                currentOffset += limit

                Handler(Looper.myLooper()!!).postDelayed({
                    loadNextPage()
                }, 1000)
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
        homeViewModel.getPokemonList().observe(viewLifecycleOwner, Observer {
//            Log.d("HomeFragment", "setObserver: ${it.size}")
//            Log.d("HomeFragment", "setObserver: ${it.last().name}")
            lastOffset = it[0].count / limit * limit
            pokemonsAdapter.addAll(it.subList(it.size - limit, it.size))
            pokemonsAdapter.removeLoadingFooter()
            isLoading = false

            if (currentOffset != lastOffset) pokemonsAdapter.addLoadingFooter()
            else isLastOffset = true
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}