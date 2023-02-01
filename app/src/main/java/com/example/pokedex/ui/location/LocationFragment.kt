package com.example.pokedex.ui.location

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
import com.example.pokedex.databinding.FragmentLocationBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.ui.recycleView.pageableitem.ListPageableItemAdapter
import com.example.pokedex.ui.recycleView.pageableitem.OnPageableItemListener
import com.example.pokedex.utils.Converter

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var locationViewModel: LocationViewModel
    private val args: LocationFragmentArgs by navArgs()

    private var locationAdapter: ListPageableItemAdapter = ListPageableItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationViewModel =
            ViewModelProvider(this).get(LocationViewModel::class.java)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textRegionName.text = Converter.beautifyName(args.regionName)

        binding.recyclerListLocation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerListLocation.adapter = locationAdapter

        val listener = object : OnPageableItemListener {
            override fun onClick(region: PageableItemDto) {
                val action = LocationFragmentDirections
                    .actionNavigationLocationToNavigationLocationArea(
                        Converter.idFromUrl(region.url),
                        region.name
                    )
                findNavController().navigate(action)
            }
        }

        binding.searchButton.setOnClickListener {
            if (binding.searchInput.text.isNotEmpty()) {
                locationViewModel.filter(binding.searchInput.text.toString())
            } else {
                locationViewModel.removeFilter()
            }
        }

        locationAdapter.setListener(listener)

        locationViewModel.loadRegion(args.regionId)

        setObserver()

        return root
    }

    /**
     * This method set the observers of the LocationFragment.
     *
     * When the locationList get updated, it should update the adapter items, with the new items.
     * There is also a filteredLocationList that update the same adapter. If user search in the bar,
     * it filter the locationList with the contained keyword, if the search bar is empty, it populate
     * the adapter with all items.
     */
    private fun setObserver() {
        locationViewModel.getLocations().observe(viewLifecycleOwner, Observer {
            locationAdapter.updateRegionList(it)
        })

        locationViewModel.getFiltered().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
                locationAdapter.updateRegionList(it)
            else
                locationAdapter.updateRegionList(locationViewModel.getLocations().value!!)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}