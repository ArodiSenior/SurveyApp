package com.arodi.surveyapp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arodi.surveyapp.repository.MainRepository
import com.arodi.surveyapp.viewmodel.MainActivityViewModel

class ViewModelFactory(private val repository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }

}