package com.example.pokedex.ui.pokemon

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonBinding
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.repository.database.model.PokemonEntity
import com.example.pokedex.repository.database.model.VarietyEntity
import com.example.pokedex.ui.recycleView.evolution.ListEvolutionAdapter
import com.example.pokedex.ui.recycleView.evolution.OnEvolutionListener
import com.example.pokedex.ui.recycleView.typeRelations.ListTypeRelationAdapter
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Converter
import com.example.pokedex.utils.Resources
import com.google.android.material.snackbar.Snackbar


class PokemonFragment : Fragment() {

    private var _binding: FragmentPokemonBinding? = null
    private lateinit var pokemonViewModel: PokemonViewModel
    private val args: PokemonFragmentArgs by navArgs()
    private val evolutionAdapter = ListEvolutionAdapter()
    private val typeRelationAdapter = ListTypeRelationAdapter()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pokemonViewModel = ViewModelProvider(this)[PokemonViewModel::class.java]

        _binding = FragmentPokemonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerListEvolutions()

        initRecyclerWeaknessAndResistances()

        configureListeners()

        setObserver()

        pokemonViewModel.loadPokemon(args.pokemonId)

        return root
    }

    private fun initRecyclerListEvolutions() {
        binding.recyclerListEvolutions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerListEvolutions.adapter = evolutionAdapter

        val listener = object : OnEvolutionListener {
            override fun onClick(evolution: EvolutionEntity) {
                if (evolution.id == args.pokemonId)
                    return

                val navBuilder = NavOptions.Builder()
                if (evolution.id > args.pokemonId)
                    navBuilder.setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_right).setPopExitAnim(R.anim.slide_out_right)
                else
                    navBuilder.setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_right)
                        .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_left)

                val action = PokemonFragmentDirections.actionPokemonFragmentSelf(evolution.id)
                findNavController().navigate(action, navBuilder.build())
            }
        }
        evolutionAdapter.setListener(listener)
    }

    private fun initRecyclerWeaknessAndResistances() {
        binding.recyclerListWeakness.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListWeakness.adapter = typeRelationAdapter
    }

    private fun configureListeners() {
        binding.shinyButton.setOnClickListener {
            pokemonViewModel.setShinyButtonToggle()
        }
        binding.femaleButton.setOnClickListener {
            pokemonViewModel.setGenderButtons(Constants.GENDERS.FEMALE)
        }
        binding.backImage.setOnClickListener {
            findNavController().navigate(R.id.action_pokemonFragment_to_navigation_home)
        }
        binding.maleButton.setOnClickListener {
            pokemonViewModel.setGenderButtons(Constants.GENDERS.MALE)
        }
    }


    private fun setObserver() {
        pokemonViewModel.getPokemon.observe(viewLifecycleOwner) {
            configurePokemonTypes(it)
            configurePokemonStats(it)
            configurePokemonGender(it)
            pokemonViewModel.configureTypeRelationList(it.typeOne, it.typeTwo)
        }
        pokemonViewModel.getImgDefault.observe(viewLifecycleOwner) {
            binding.pokemonImage.setImageBitmap(it)
        }
        pokemonViewModel.getShinyButton.observe(viewLifecycleOwner) {
            configurePokemonImage(it, pokemonViewModel.getGenderButtons.value!!)
            configureShinyImage(it)
        }
        pokemonViewModel.getGenderButtons.observe(viewLifecycleOwner) {
            configurePokemonImage(pokemonViewModel.getShinyButton.value!!, it)
            configureGenderButtons(it)
        }
        pokemonViewModel.getCustomEvolutionList.observe(viewLifecycleOwner) {
            evolutionAdapter.updateEvolutionList(it)
        }
        pokemonViewModel.getVarietyList.observe(viewLifecycleOwner) {
            configureVarietyDropdown(it)
        }
        pokemonViewModel.getImgDefault.observe(viewLifecycleOwner) {
            configurePokemonImage(pokemonViewModel.getShinyButton.value!!, pokemonViewModel.getGenderButtons.value!!)
        }
        pokemonViewModel.getImgShiny.observe(viewLifecycleOwner) {
            configurePokemonImage(pokemonViewModel.getShinyButton.value!!, pokemonViewModel.getGenderButtons.value!!)
        }
        pokemonViewModel.getImgFemale.observe(viewLifecycleOwner) {
            configurePokemonImage(pokemonViewModel.getShinyButton.value!!, pokemonViewModel.getGenderButtons.value!!)
        }
        pokemonViewModel.getImgShinyFemale.observe(viewLifecycleOwner) {
            configurePokemonImage(pokemonViewModel.getShinyButton.value!!, pokemonViewModel.getGenderButtons.value!!)
        }
        pokemonViewModel.getTypeRelationList.observe(viewLifecycleOwner) { it ->
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {
                typeColors.add(
                    resources.getColor(
                        Resources.getColorByName(it.name), null
                    )
                )
                typeStrings.add(
                    resources.getString(
                        Resources.getStringByName(it.name)
                    )
                )
            }
            typeRelationAdapter.updateColorsList(typeColors.toList())
            typeRelationAdapter.updateNamesList(typeStrings.toList())
            typeRelationAdapter.updateTypeList(it)
        }
        pokemonViewModel.getStatusMessage.observe(viewLifecycleOwner) {
            if (it.code != Constants.DB_MSGS.SUCCESS && it.code != Constants.API_MSGS.SUCCESS) {
                showSnackBar(
                    resources.getString(Resources.getErrorMessageByStatusMessage(it))
                        .replace("{{id}}", it.item.toString())
                )
            }
        }
    }

    private fun configureVarietyDropdown(varietyList: List<VarietyEntity>) {
        val dropdownVarieties = binding.dropdownVarieties
        val pokemonNameList = mutableListOf(varietyList.find { it.id == args.pokemonId }?.pokemonName ?: "")
        pokemonNameList.addAll(varietyList.map { it.pokemonName }.filter { it != pokemonNameList[0] })

        val dropdownAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, pokemonNameList
            )

        dropdownAdapter
            .setDropDownViewResource(R.layout.spinner_item)
        dropdownVarieties.adapter = dropdownAdapter

        dropdownVarieties.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                val selectedPokemonId = varietyList.find { it.pokemonName == selectedItem }?.id

                if (selectedPokemonId != null && selectedPokemonId != args.pokemonId) {
                    val navBuilder = NavOptions.Builder()
                    navBuilder.setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out)
                            .setPopEnterAnim(android.R.anim.fade_in).setPopExitAnim(android.R.anim.fade_out)
                    val action = PokemonFragmentDirections.actionPokemonFragmentSelf(selectedPokemonId)
                    findNavController().navigate(action, navBuilder.build())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }
    }

    private fun configurePokemonTypes(pokemonEntity: PokemonEntity){
        binding.pokemonNumber.text = Converter.idFromUrl(pokemonEntity.speciesUrl).toString().padStart(4, '0')
        binding.pokemonType1.text = resources.getString(Resources.getStringByName(pokemonEntity.typeOne))
        binding.pokemonType1.setBackgroundColor(resources.getColor(Resources.getColorByName(pokemonEntity.typeOne), null))
        if (pokemonEntity.typeTwo != null) {
            binding.pokemonType2.text = resources.getString(Resources.getStringByName(pokemonEntity.typeTwo!!))
            binding.pokemonType2.setBackgroundColor(resources.getColor(Resources.getColorByName(pokemonEntity.typeTwo!!), null))
        }else{
            binding.pokemonType2.visibility = View.GONE
        }
    }

    private fun configurePokemonStats(pokemonEntity: PokemonEntity){
        binding.hpValue.text = pokemonEntity.statHp.toString()
        binding.hpBar.progress = pokemonEntity.statHp * 100 / 255
        binding.attackValue.text = pokemonEntity.statAttack.toString()
        binding.attackBar.progress = pokemonEntity.statAttack * 100 / 255
        binding.defenseValue.text = pokemonEntity.statDefense.toString()
        binding.defenseBar.progress = pokemonEntity.statDefense * 100 / 255
        binding.spAtkValue.text = pokemonEntity.statSpAttack.toString()
        binding.spAtkBar.progress = pokemonEntity.statSpAttack * 100 / 255
        binding.spDefValue.text = pokemonEntity.statSpDefense.toString()
        binding.spDefBar.progress = pokemonEntity.statSpDefense * 100 / 255
        binding.speedValue.text = pokemonEntity.statSpeed.toString()
        binding.speedBar.progress = pokemonEntity.statSpeed * 100 / 255

    }

    private fun configurePokemonGender(pokemonEntity: PokemonEntity) {
        when (pokemonEntity.genderRate) {
            -1 -> {
                binding.genderLine.visibility = View.GONE
                pokemonViewModel.setGenderButtons(Constants.GENDERS.GENDERLESS)
            }
            0 -> {
                binding.femaleButton.visibility = View.GONE
                pokemonViewModel.setGenderButtons(Constants.GENDERS.MALE)
            }
            8 -> {
                binding.maleButton.visibility = View.GONE
                pokemonViewModel.setGenderButtons(Constants.GENDERS.FEMALE)
            }
        }
    }

    private fun configureShinyImage(shineButtonState: Boolean){
        if (shineButtonState) {
            binding.shinyButton.setImageResource(R.drawable.ic_shiny_checked)
        }else{
            binding.shinyButton.setImageResource(R.drawable.ic_shiny)
        }
    }

    private fun configureGenderButtons(genderButtonsState: Int) {
        when (genderButtonsState) {
            Constants.GENDERS.MALE -> {
                binding.maleButton.setBackgroundColor(resources.getColor(R.color.male, null))
                binding.maleButton.setColorFilter(resources.getColor(R.color.white, null))
                binding.femaleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.femaleButton.setColorFilter(resources.getColor(R.color.female, null))
            }
            Constants.GENDERS.FEMALE -> {
                binding.maleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.maleButton.setColorFilter(resources.getColor(R.color.male, null))
                binding.femaleButton.setBackgroundColor(resources.getColor(R.color.female, null))
                binding.femaleButton.setColorFilter(resources.getColor(R.color.white, null))
            }
            else -> {
                binding.maleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.maleButton.setColorFilter(resources.getColor(R.color.male, null))
                binding.femaleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.femaleButton.setColorFilter(resources.getColor(R.color.female, null))
            }
        }
    }

    private fun configurePokemonImage(shineButtonState: Boolean, genderButtonsState: Int){
        if(shineButtonState && genderButtonsState != Constants.GENDERS.FEMALE)
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgShiny.value)
        else if (shineButtonState && genderButtonsState == Constants.GENDERS.FEMALE)
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgShinyFemale.value)
        else if (!shineButtonState && genderButtonsState != Constants.GENDERS.FEMALE)
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgDefault.value)
        else {
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgFemale.value)
        }
    }

    private fun showSnackBar(text: String) {
        val snack = Snackbar.make(binding.pokemonFragment, text, Snackbar.LENGTH_SHORT)
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