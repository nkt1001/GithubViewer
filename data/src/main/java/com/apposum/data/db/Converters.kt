package com.apposum.data.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }
}