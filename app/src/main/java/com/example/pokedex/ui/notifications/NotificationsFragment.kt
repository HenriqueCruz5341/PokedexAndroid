package com.example.pokedex.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.FragmentNotificationsBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.ui.recycleView.region.ListRegionAdapter
import com.example.pokedex.ui.recycleView.region.OnRegionListener
import com.example.pokedex.utils.Converter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var notificationsViewModel: NotificationsViewModel

    private var regionAdapter: ListRegionAdapter = ListRegionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListRegion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerListRegion.adapter = regionAdapter

        val listener = object : OnRegionListener {
            override fun onClick(region: PageableItemDto) {
                 val action = NotificationsFragmentDirections
                     .actionNavigationNotificationsToNavigationLocation(
                         Converter.idFromUrl(region.url),
                         region.name
                     )
                findNavController().navigate(action)
            }
        }

        regionAdapter.setListener(listener)

        notificationsViewModel.loadRegions()

        setObserver()

        return root
    }

    private fun setObserver() {
        notificationsViewModel.getRegions().observe(viewLifecycleOwner, Observer {
            regionAdapter.updateRegionList(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}