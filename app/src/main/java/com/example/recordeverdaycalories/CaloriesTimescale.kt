package com.example.recordeverdaycalories

import java.util.*

class CaloriesTimescale{
    var date: Date
    var calories: Int = 0

    constructor(date: Date, calories: Int) {
        this.date = date
        this.calories = calories
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}