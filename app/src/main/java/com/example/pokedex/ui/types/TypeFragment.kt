package com.example.pokedex.ui.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.databinding.FragmentTypesBinding
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.ui.recycleView.typeRelations.ListTypeRelationAdapter
import com.example.pokedex.ui.recycleView.types.ListTypesAdapter
import com.example.pokedex.ui.recycleView.types.OnTypeListener

class TypeFragment : Fragment() {

    private var _binding: FragmentTypesBinding? = null
    private lateinit var typeViewModel: TypeViewModel
    private val typeAdapter = ListTypesAdapter()
    private val typeDefenseAdapter = ListTypeRelationAdapter()
    private val typeAttackAdapter = ListTypeRelationAdapter()
    private var selectedType: TypeEntity? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        typeViewModel = ViewModelProvider(this)[TypeViewModel::class.java]

        _binding = FragmentTypesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListTypes.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListTypes.adapter = typeAdapter

        binding.recyclerListDefense.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListDefense.adapter = typeDefenseAdapter

        binding.recyclerListAttack.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListAttack.adapter = typeAttackAdapter

        val listener = object : OnTypeListener {
            override fun onClick(type: TypeEntity) {
                typeViewModel.userSelectType(type)
            }
        }
        typeAdapter.setListener(listener)

        typeViewModel.getAllTypes()

        setObserver()

        return root
    }

    /**
     * This method set the observers of the TypeViewModel.
     *
     * When the one of the lists get changed, it should update the Colors and Name list as well.
     * When selectedTypes list have one value, Attacker list should be shown, otherwise, it should
     * be hidden.
     */
    private fun setObserver() {
        typeViewModel.getTypeList().observe(viewLifecycleOwner, Observer {
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {it2 ->
                typeColors.add(
                    getTypeColor(it2.name)
                )
                typeStrings.add(
                    getTranslatedName(it2.name)
                )
            }
            typeAdapter.updateColors(typeColors.toList())
            typeAdapter.updateNames(typeStrings.toList())
            typeAdapter.updateTypeList(it)
        })

        typeViewModel.getTypeEffectiveness().observe(viewLifecycleOwner, Observer {
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {it2 ->
                typeColors.add(
                    getTypeColor(it2.name)
                )
                typeStrings.add(
                    getTranslatedName(it2.name)
                )
            }
            typeAttackAdapter.updateColorsList(typeColors.toList())
            typeAttackAdapter.updateNamesList(typeStrings.toList())
            typeAttackAdapter.updateTypeList(it)
        })

        typeViewModel.getTypeWeakness().observe(viewLifecycleOwner, Observer {
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {it2 ->
                typeColors.add(
                    getTypeColor(it2.name)
                )
                typeStrings.add(
                    getTranslatedName(it2.name)
                )
            }
            typeDefenseAdapter.updateColorsList(typeColors.toList())
            typeDefenseAdapter.updateNamesList(typeStrings.toList())
            typeDefenseAdapter.updateTypeList(it)
        })

        typeViewModel.getSelectedTypeList().observe(viewLifecycleOwner, Observer {
            if(it.size != 1) {
                binding.recyclerListAttack.visibility = View.GONE
                binding.textAttack.visibility = View.GONE
            } else {
                binding.recyclerListAttack.visibility = View.VISIBLE
                binding.textAttack.visibility = View.VISIBLE
            }
            typeAdapter.select(it)
        })
    }

    /**
     * This method get Color Int value from resources, based on type name.
     *
     * @param name Default type name in english lowercase.
     * @return integer value of the type color
     */
    private fun getTypeColor(name: String): Int {
        return resources.getColor(
            com.example.pokedex.utils.Resources.getColorByName(name), null
        )
    }

    /**
     * This method get type translated Name based on default type name.
     *
     * @param name Default type name in english lowercase.
     * @return translated name of the type
     */
    private fun getTranslatedName(name: String): String {
        return resources.getString(
            com.example.pokedex.utils.Resources.getStringByName(name)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
