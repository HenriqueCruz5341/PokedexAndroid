package com.example.pokedex.ui.locationarea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.FragmentLocationareaBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.ui.recycleView.pageableitem.ListPageableItemAdapter
import com.example.pokedex.ui.recycleView.pageableitem.OnPageableItemListener
import com.example.pokedex.utils.Converter

class LocationAreaFragment : Fragment() {

    private var _binding: FragmentLocationareaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var locationAreaViewModel: LocationAreaViewModel
    private val args: LocationAreaFragmentArgs by navArgs()

    private var locationAreaAdapter: ListPageableItemAdapter = ListPageableItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationAreaViewModel =
            ViewModelProvider(this).get(LocationAreaViewModel::class.java)

        _binding = FragmentLocationareaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textLocationName.text = args.locationName

        binding.recyclerListLocationArea.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerListLocationArea.adapter = locationAreaAdapter

        val listener = object : OnPageableItemListener {
            override fun onClick(region: PageableItemDto) {
                val action = LocationAreaFragmentDirections
                    .actionNavigationLocationAreaToNavigationLocationPokemon(
                        Converter.idFromUrl(region.url),
                        region.name
                    )
                findNavController().navigate(action)
            }
        }

        locationAreaAdapter.setListener(listener)

        locationAreaViewModel.loadLocation(args.locationId)

        setObserver()

        return root
    }

    private fun setObserver() {
        locationAreaViewModel.getLocationAreas().observe(viewLifecycleOwner, Observer {
            locationAreaAdapter.updateRegionList(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}