package com.collegecapstoneteam1.cookingapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.collegecapstoneteam1.cookingapp.R
import com.collegecapstoneteam1.cookingapp.data.repository.RecipeRepositoryImpl
import com.collegecapstoneteam1.cookingapp.databinding.ActivityMainBinding
import com.collegecapstoneteam1.cookingapp.ui.viewmodel.MainViewModel
import com.collegecapstoneteam1.cookingapp.ui.viewmodel.MainViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val recipeRepositoryImpl = RecipeRepositoryImpl()
    private val factory = MainViewModelProviderFactory(recipeRepositoryImpl)
    val viewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupJetpackNavigation()

        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
        }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

    }

    private fun setupJetpackNavigation(){
        val host = supportFragmentManager
            .findFragmentById(R.id.cookingsearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}