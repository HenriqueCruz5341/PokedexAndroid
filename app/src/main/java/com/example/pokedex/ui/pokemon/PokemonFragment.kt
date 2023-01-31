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
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO
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


/**
 * Pokemon Fragment is the fragment that shows the details of a pokemon.
 *
 * It shows the name, id, image, types, gender, stats, evolutions and weaknesses and resistances of
 * the pokemon. It also allows the user to change the variety of the pokemon. And navigate to the
 * details of the evolutions.
 *
 * @property pokemonViewModel the view model of the pokemon fragment
 * @property args the arguments of the pokemon fragment used to get the pokemon id from the navigation
 * @property evolutionAdapter the adapter of the recycler view of the evolutions
 * @property typeRelationAdapter the adapter of the recycler view of the weaknesses and resistances
 * @property binding the binding of the pokemon fragment
 */
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

    /**
     * This method init the recycler view of the evolutions.
     *
     * It sets the layout manager and the adapter of the recycler view. It also sets the adapter and
     * listener to navigate to the details of the evolutions, and the animations of the navigation.
     * The animations are different depending on the direction of the navigation. If the evolution
     * is the next one the animation is to the left, if it is the previous one the animation is to
     * the right. If the evolution is the same as the pokemon the navigation is not done.
     */
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

    /**
     * This method init the recycler view of the weaknesses and resistances.
     *
     * It sets the layout manager and the adapter of the recycler view.
     */
    private fun initRecyclerWeaknessAndResistances() {
        binding.recyclerListWeakness.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListWeakness.adapter = typeRelationAdapter
    }

    /**
     * This method configures the listeners of the buttons of the pokemon fragment.
     *
     * It sets listeners to change pokemon sprite, according to the gender and the shiny state. It
     * also sets a listener to navigate to the home fragment.
     */
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

    /**
     * This method sets all the observers of the pokemon fragment.
     *
     * It sets the observer of the pokemon, the observer of the default, shiny, female and shine
     * female sprite, the observer of the state of the shiny and gender buttons, the observer of
     * the list of evolutions, the observer of the list of weaknesses and resistances and the
     * observer of the error message, that shows a SnackBar with the error message.
     */
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
            configureTypeRelation(it)
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

    /**
     * This method configures the weakness and resistances recycler view.
     *
     * It sets the colors and names of the types and the list of the type multipliers. The colors
     * and names are set according to the type multiplier list, and must be passable to the adapter,
     * because is not possible access Resources outside a Fragment.
     *
     * @param typeMultiplierList The list of the type multipliers.
     */
    private fun configureTypeRelation(typeMultiplierList: List<TypeMultiplierDTO>) {
        val typeColors: MutableList<Int> = mutableListOf()
        val typeStrings: MutableList<String> = mutableListOf()
        typeMultiplierList.forEach {
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
        typeRelationAdapter.updateTypeList(typeMultiplierList)
    }

    /**
     * This method configures the pokemon variety dropdown.
     *
     * It sets the adapter of the dropdown and sets a listener to change the pokemon when the user
     * selects a different variety. If selected a different variety, the pokemon is set to the
     * selected variety navigating to the pokemon fragment. If the selected variety is the same as
     * the current pokemon, the pokemon is not changed.
     *
     * @param varietyList The list of the pokemon varieties.
     */
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

    /**
     * This method configures the pokemon types.
     *
     * It sets the pokemon types and colors according to the pokemon type. If the pokemon has two
     * types, the second type is set and visible. If the pokemon has only one type, the second type
     * is set to invisible.
     *
     * @param pokemonEntity The pokemon entity.
     */
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

    /**
     * This method configures the pokemon stats.
     *
     * It sets the pokemon stats and progress bars according to the pokemon stats. The progress bars
     * are set according to the stats, and the stats are set according to the pokemon stats.
     *
     * @param pokemonEntity The pokemon entity.
     */
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

    /**
     * This method configures the pokemon gender.
     *
     * It controls the visibility of each button. If pokemon has no gender, all buttons are set to
     * invisible, if pokemons has only male gender, the female button is set to invisible, and if
     * pokemon has only female gender, the male button is set to invisible. Then a method from
     * pokemon view model is called to set status to each state.
     *
     * @param pokemonEntity The pokemon entity.
     */
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

    /**
     * This method configures the shiny button image.
     *
     * It controls the image resource of the shiny button. If pokemon is shiny, the shiny button
     * image is set to checked, if pokemon is not shiny, the shiny button image is set to unchecked.
     *
     * @param shineButtonState The shiny button state.
     */
    private fun configureShinyImage(shineButtonState: Boolean){
        if (shineButtonState) {
            binding.shinyButton.setImageResource(R.drawable.ic_shiny_checked)
        }else{
            binding.shinyButton.setImageResource(R.drawable.ic_shiny)
        }
    }

    /**
     * This method configures the gender buttons colors.
     *
     * If the gender button state is set to male, the male button must has a background color and
     * icon set to white, and the female button must has a background color set to transparent and
     * icon set to female color. The logic must reverse otherwise.
     *
     * @param genderButtonsState the gender button state according Constants.GENDERS.
     * @see Constants.GENDERS
     */
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

    /**
     * This method configures the pokemon image.
     *
     * It displays pokemon image according to shine and gender buttons state. If shine button is
     * true, the shine image is displayed, if shine button is false, the default image is displayed.
     * If the gender button state is male, default image is displayed, if female, female image is
     * displayed. The shine and gender state can be combined.
     *
     * @param shineButtonState The shiny button state.
     * @param genderButtonsState The gender button state.
     */
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

    /**
     * This method shows a SnackBar with the text passed as parameter.
     *
     * @param text The text to show in the SnackBar.
     */
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