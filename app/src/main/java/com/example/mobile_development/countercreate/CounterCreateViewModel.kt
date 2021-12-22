package com.example.mobile_development.countercreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_development.database.CounterDatabaseDao
import com.example.mobile_development.database.Counter
import kotlinx.coroutines.launch

class CounterCreateViewModel(
    dataSource: CounterDatabaseDao
) : ViewModel() {

    val database = dataSource

    private val _eventCreate = MutableLiveData<Boolean>()
    val eventCreate: LiveData<Boolean>
        get() = _eventCreate

    private val _eventCancel = MutableLiveData<Boolean>()
    val eventCancel: LiveData<Boolean>
        get() = _eventCancel

    private val _eventColor = MutableLiveData<Boolean>()
    val eventColor: LiveData<Boolean>
        get() = _eventColor

    private val _eventAdvanced = MutableLiveData<Boolean>()
    val eventAdvanced: LiveData<Boolean>
        get() = _eventAdvanced


    fun onCreate() {
        _eventCreate.value = true
    }

    fun onCreateComplete() {
        _eventCreate.value = false
    }

    fun onCancel() {
        _eventCancel.value = true
    }

    fun onCancelComplete() {
        _eventCancel.value = false
    }

    fun onColor() {
        _eventColor.value = true
    }

    fun onColorComplete() {
        _eventColor.value = false
    }

    fun onAdvanced() {
        _eventAdvanced.value = true
    }

    fun onAdvancedComplete() {
        _eventAdvanced.value = false
    }


    fun createCounter(
        name: String,
        color: Int,
        startingValue: Int,
        increaseValue: Int,
        decreaseValue: Int,
        goalValue: Int
    ) {
        viewModelScope.launch {
            val newCounter = Counter(name, color, startingValue, increaseValue, decreaseValue, goalValue)

            insert(newCounter)

        }

    }

    private suspend fun insert(counter: Counter) {
        database.insert(counter)
    }


}