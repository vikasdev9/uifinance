package com.uifinance.project291.data.local

import androidx.room.TypeConverter
import com.uifinance.project291.data.local.entity.CategoryType
import com.uifinance.project291.data.local.entity.RecurrenceType
import com.uifinance.project291.data.local.entity.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }


    @TypeConverter
    fun fromRecurrence(value: String): RecurrenceType {
        return RecurrenceType.valueOf(value)
    }

    @TypeConverter
    fun recurrenceToString(recurrence: RecurrenceType): String {
        return recurrence.name
    }

    @TypeConverter
    fun fromTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun transactionTypeToString(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun fromCategoryType(value: String): CategoryType {
        return CategoryType.valueOf(value)
    }

    @TypeConverter
    fun categoryTypeToString(type: CategoryType): String {
        return type.name
    }
}
