package com.example.mobile_development.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CounterDatabaseDao {

    @Insert
    suspend fun insert(counter: Counter)

    @Update
    suspend fun update(counter: Counter)

    @Query("SELECT * FROM counter_table WHERE counterId= :key")
    suspend fun get(key: Long): Counter

    @Query("DELETE FROM counter_table WHERE counterId = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM counter_table")
    suspend fun clear()

    @Query("SELECT * FROM counter_table ORDER BY counterId DESC")
    fun getAllCounters(): LiveData<List<Counter>>

    @Query("SELECT * FROM counter_table WHERE counterId=:key")
    fun getCounterWithId(key: Long): LiveData<Counter>
}