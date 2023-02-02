package com.example.pokedex.ui.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.databinding.FragmentRegionBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.ui.recycleView.pageableitem.ListPageableItemAdapter
import com.example.pokedex.ui.recycleView.pageableitem.OnPageableItemListener
import com.example.pokedex.utils.Converter

class RegionFragment : Fragment() {

    private var _binding: FragmentRegionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var regionViewModel: RegionViewModel

    private var regionAdapter: ListPageableItemAdapter = ListPageableItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        regionViewModel =
            ViewModelProvider(this).get(RegionViewModel::class.java)

        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListRegion.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerListRegion.adapter = regionAdapter

        val listener = object : OnPageableItemListener {
            override fun onClick(region: PageableItemDto) {
                 val action = RegionFragmentDirections
                     .actionNavigationNotificationsToNavigationLocation(
                         Converter.idFromUrl(region.url),
                         region.name
                     )
                findNavController().navigate(action)
            }
        }

        regionAdapter.setListener(listener)

        regionViewModel.loadRegions()

        setObserver()

        return root
    }

    private fun setObserver() {
        regionViewModel.getRegions().observe(viewLifecycleOwner) {
            regionAdapter.updateRegionList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}