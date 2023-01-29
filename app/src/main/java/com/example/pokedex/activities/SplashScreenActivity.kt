package com.example.pokedex.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.R
import com.example.pokedex.activities.viewModel.SplashScreenViewModel
import com.example.pokedex.databinding.ActivitySplashScreenBinding
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Resources
import com.google.android.material.snackbar.Snackbar

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var splashScreenVM: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashScreenVM = ViewModelProvider(this)[SplashScreenViewModel::class.java]
        supportActionBar?.hide()

        val topPage = binding.topPage
        val lockPokeball = binding.lockPokeball

        val screenHeight = resources.displayMetrics.heightPixels

        val topPageAnim = ObjectAnimator.ofFloat(topPage, "translationY", -screenHeight / 2.0f)
        topPageAnim.duration = 2000L
        topPageAnim.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        val lockPokeballAnim =
            ObjectAnimator.ofFloat(lockPokeball, "translationY", screenHeight / 2.0f)
        lockPokeballAnim.duration = 2000L
        lockPokeballAnim.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        splashScreenVM.saveTypes()
        splashScreenVM.requestPokemons()

        splashScreenVM.getApiMsg.observe(this) {
            Handler(Looper.getMainLooper()).postDelayed({
                topPageAnim.start()
                lockPokeballAnim.start()
            }, 1000L)

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, 3200L)

            if (it.code != Constants.DB_MSGS.SUCCESS && it.code != Constants.API_MSGS.SUCCESS) {
                showSnackBar(
                    resources.getString(Resources.getErrorMessageByStatusMessage(it))
                        .replace("{{id}}", it.item.toString())
                )
            }
        }
        splashScreenVM.getDbMsg.observe(this) {
            if (it.code != Constants.DB_MSGS.SUCCESS && it.code != Constants.API_MSGS.SUCCESS) {
                showSnackBar(
                    resources.getString(Resources.getErrorMessageByStatusMessage(it))
                        .replace("{{id}}", it.item.toString())
                )
            }
        }
    }

    private fun showSnackBar(text: String) {
        val snack = Snackbar.make(binding.splashScreen, text, Snackbar.LENGTH_SHORT)
        snack.setBackgroundTint(resources.getColor(R.color.red, null))
        snack.setTextColor(Color.WHITE)
        snack.setTextMaxLines(2)

        snack.show()
    }
}
