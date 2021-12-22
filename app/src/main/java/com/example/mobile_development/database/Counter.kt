package com.example.mobile_development.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter_table")
data class Counter(
    @PrimaryKey(autoGenerate = true)
    var counterId: Long = 0L,

    @ColumnInfo(name = "name")
    var counterName: String = "",

    @ColumnInfo(name = "color")
    var color: Int = 0,

    @ColumnInfo(name = "initial_value")
    var initialValue: Int = 0,

    @ColumnInfo(name = "current_value")
    var currentValue: Int = 0,

    @ColumnInfo(name = "increment_value")
    var incrementValue: Int = 1,

    @ColumnInfo(name = "decrement_value")
    var decrementValue: Int = 1,

    @ColumnInfo(name="goal_value")
    var goalValue: Int = Int.MAX_VALUE,

    @ColumnInfo(name="goal_reached")
    var goalReached: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "tapped_at")
    var tappedAt: Long = 0L,

    @ColumnInfo(name = "total_taps")
    var totalTaps: Long = 0
) {
    constructor(
        name: String,
        color: Int,
        initialValue: Int,
        incrementValue: Int,
        decrementValue: Int,
        goalValue: Int
    ) : this() {
        this.counterName = name
        this.color = color
        this.initialValue = initialValue
        this.currentValue = initialValue
        this.incrementValue = incrementValue
        this.decrementValue = decrementValue
        this.goalValue = goalValue
    }
}

