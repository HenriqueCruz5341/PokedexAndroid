package com.example.pokedex.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.FragmentLocationBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.ui.recycleView.region.ListRegionAdapter
import com.example.pokedex.ui.recycleView.region.OnRegionListener

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var locationViewModel: LocationViewModel
    private val args: LocationFragmentArgs by navArgs()

    private var locationAdapter: ListRegionAdapter = ListRegionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationViewModel =
            ViewModelProvider(this).get(LocationViewModel::class.java)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListLocation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerListLocation.adapter = locationAdapter

        val listener = object : OnRegionListener {
            override fun onClick(region: PageableItemDto) {
                // val action =
                // findNavController().navigate(action)
                println(region)
            }
        }

        locationAdapter.setListener(listener)

        locationViewModel.loadRegion(args.regionId)

        setObserver()

        return root
    }

    private fun setObserver() {
        locationViewModel.getLocations().observe(viewLifecycleOwner, Observer {
            locationAdapter.updateRegionList(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}