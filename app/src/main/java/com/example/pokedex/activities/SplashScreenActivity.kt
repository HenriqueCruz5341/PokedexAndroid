package com.example.pokedex.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.activities.viewModel.SplashScreenViewModel
import com.example.pokedex.databinding.ActivitySplashScreenBinding
import java.io.Serializable

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashScreenVM: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashScreenVM = ViewModelProvider(this)[SplashScreenViewModel::class.java]

        supportActionBar?.hide()

        val topPage = binding.topPage
        val lockPokeball = binding.lockPokeball

        val screenHeight = resources.displayMetrics.heightPixels

        val topPageAnim = ObjectAnimator.ofFloat(topPage, "translationY", -screenHeight/2.0f)
        topPageAnim.duration = 2000L
        topPageAnim.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        val lockPokeballAnim = ObjectAnimator.ofFloat(lockPokeball, "translationY", screenHeight/2.0f)
        lockPokeballAnim.duration = 2000L
        lockPokeballAnim.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        splashScreenVM.requestPokemons(object: MyCallback {
            override fun run () {
                Handler(Looper.getMainLooper()).postDelayed({
                    topPageAnim.start()
                    lockPokeballAnim.start()
                }, 1000L)

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }, 3200L)
            }
        })
    }
}

interface MyCallback {
    fun run()
}