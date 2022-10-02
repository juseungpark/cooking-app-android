package com.collegecapstoneteam1.cookingapp.ui.viewmodel

import androidx.lifecycle.*
import com.collegecapstoneteam1.cookingapp.data.repository.RecipeRepository

class MainViewModelProviderFactory(
    private val recipeRepository: RecipeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(recipeRepository) as T
        }
        throw java.lang.IllegalArgumentException("ViewModel class not found")
    }
}