package com.example.pokedex.ui.locationpokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.FragmentLocationPokemonBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.ui.recycleView.region.ListRegionAdapter
import com.example.pokedex.ui.recycleView.region.OnRegionListener

class LocationPokemonFragment : Fragment() {

    private var _binding: FragmentLocationPokemonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var locationPokemonViewModel: LocationPokemonViewModel
    private val args: LocationPokemonFragmentArgs by navArgs()

    private var pokemonAdapter: ListRegionAdapter = ListRegionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationPokemonViewModel =
            ViewModelProvider(this).get(LocationPokemonViewModel::class.java)

        _binding = FragmentLocationPokemonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textLocationAreaName.text = args.locationAreaName

        binding.recyclerListLocationPokemon.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        binding.recyclerListLocationPokemon.adapter = pokemonAdapter

        val listener = object : OnRegionListener {
            override fun onClick(region: PageableItemDto) {
                // val action =
                // findNavController().navigate(action)
                println(region)
            }
        }

        pokemonAdapter.setListener(listener)

        locationPokemonViewModel.loadLocationArea(args.locationAreaId)

        setObserver()

        return root
    }

    private fun setObserver() {
        locationPokemonViewModel.getPokemon().observe(viewLifecycleOwner, Observer {
            pokemonAdapter.updateRegionList(it)
            println("entrei aqui")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}