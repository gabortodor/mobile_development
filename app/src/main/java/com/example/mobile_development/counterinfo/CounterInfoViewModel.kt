package com.example.mobile_development.counterinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_development.database.Counter
import com.example.mobile_development.database.CounterDatabaseDao
import com.example.mobile_development.network.FactApi
import kotlinx.coroutines.launch


class CounterInfoViewModel(
    private val counterId: Long = 0L,
    dataSource: CounterDatabaseDao
) : ViewModel() {

    val UPDATE_HIDE: Int = 0
    val UPDATE_START: Int = 1
    val UPDATE_FINISH: Int = 2

    val database = dataSource

    private val counter: LiveData<Counter>

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _eventDelete = MutableLiveData<Boolean>()
    val eventDelete: LiveData<Boolean>
        get() = _eventDelete

    private val _eventUpdate = MutableLiveData<Int>()
    val eventUpdate: LiveData<Int>
        get() = _eventUpdate


    fun onDelete() {
        _eventDelete.value = true
    }

    fun onDeleteComplete() {
        viewModelScope.launch {
            delete(counterId)
            _eventDelete.value = false
        }
    }

    fun onUpdate() {
        _eventUpdate.value = UPDATE_START
    }

    fun onUpdateComplete() {
        _eventUpdate.value = UPDATE_FINISH
    }

    fun onUpdateHide() {
        _eventUpdate.value = UPDATE_HIDE
    }

    fun onReset() {
        viewModelScope.launch {
            counter.value?.let { reset(it) }
        }
    }

    fun getCounter() = counter

    init {
        counter = database.getCounterWithId(counterId)
        getFact()

    }

    fun updateCounter(
        name: String,
        value: String,
        decreaseValue: String,
        increaseValue: String,
        goalValue: String
    ) {
        val newCounter = counter.value!!
        if (name != "")
            newCounter.counterName = name
        if (value != "")
            newCounter.currentValue = value.toInt()
        if (decreaseValue != "")
            newCounter.decrementValue = decreaseValue.toInt()
        if (increaseValue != "")
            newCounter.incrementValue = increaseValue.toInt()
        if (goalValue != "")
            newCounter.goalValue = goalValue.toInt()

        viewModelScope.launch {
            update(newCounter)
        }
    }

    suspend fun reset(counter: Counter) {
        counter.currentValue = counter.initialValue
        database.update(counter)
    }

    suspend fun update(counter: Counter) {
        database.update(counter)
    }

    suspend fun delete(counterId: Long) {
        database.deleteById(counterId)
    }


    private fun getFact() {
        viewModelScope.launch {
            try {
                val resultFact = FactApi.retrofitService.getFact()
                _response.value = resultFact.text
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

}