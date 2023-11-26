package com.example.mcproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExportViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExportViewModel::class.java)) {
            return ExportViewModel() as T
        }
        return super.create(modelClass)
    }

}