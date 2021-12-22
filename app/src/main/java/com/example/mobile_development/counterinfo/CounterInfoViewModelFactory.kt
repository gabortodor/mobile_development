package com.example.mobile_development.counterinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_development.database.CounterDatabaseDao

class CounterInfoViewModelFactory(
    private val counterId: Long,
    private val dataSource: CounterDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterInfoViewModel::class.java)) {
            return CounterInfoViewModel(counterId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}