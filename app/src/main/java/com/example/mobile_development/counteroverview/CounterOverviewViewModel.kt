package com.example.mobile_development.counteroverview

import androidx.lifecycle.*
import com.example.mobile_development.database.Counter
import com.example.mobile_development.database.CounterDatabaseDao
import kotlinx.coroutines.launch

class CounterOverviewViewModel(
    dataSource: CounterDatabaseDao
) : ViewModel() {

    val database = dataSource

    val counters = database.getAllCounters()


    private val _eventCreate = MutableLiveData<Boolean>()
    val eventCreate: LiveData<Boolean>
        get() = _eventCreate

    private val _eventRestart = MutableLiveData<Boolean>()
    val eventRestart: LiveData<Boolean>
        get() = _eventRestart

    private val _eventClear = MutableLiveData<Boolean>()
    val eventClear: LiveData<Boolean>
        get() = _eventClear


    val countersEmpty = Transformations.map(counters) {
       it.isEmpty()
    }

    fun onCreate() {
        _eventCreate.value = true
    }

    fun onCreateComplete() {
        _eventCreate.value = false
    }

    fun onRestart() {
        _eventRestart.value = true
    }

    fun onRestartComplete() {
        _eventRestart.value = false
    }

    fun onClear() {
        _eventClear.value = true
    }

    fun onClearComplete() {
        viewModelScope.launch {
            clear()
        }
        _eventClear.value = false
    }

    fun incrementCounter(counterId: Long) {
        viewModelScope.launch {
            var counter = get(counterId)
            counter.currentValue = counter.currentValue + counter.incrementValue
            if (counter.currentValue >= counter.goalValue) {
                counter.currentValue = counter.initialValue
                counter.goalReached += 1
                onRestart()
            }
            counter.tappedAt = System.currentTimeMillis()
            counter.totalTaps += 1
            update(counter)
        }
    }

    fun decrementCounter(counterId: Long) {
        viewModelScope.launch {
            var counter = get(counterId)
            counter.currentValue = counter.currentValue - counter.decrementValue
            counter.tappedAt = System.currentTimeMillis()
            counter.totalTaps += 1
            update(counter)
        }
    }

    private suspend fun clear(){
        database.clear()
    }

    private suspend fun update(counter: Counter) {
        database.update(counter)
    }

    private suspend fun get(counterId: Long): Counter {
        return database.get(counterId)
    }
}