package com.example.orgs.data.database.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromDoubleToBigDecimal(value: Double?): BigDecimal {
        return value?.let { BigDecimal(value.toString()) } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun fromBigDecimalToDouble(value: BigDecimal?): Double {
        return value?.let { value.toDouble() } ?: 0.0
    }
}