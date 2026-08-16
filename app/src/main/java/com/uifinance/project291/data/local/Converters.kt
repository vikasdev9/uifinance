package com.uifinance.project291.data.local

import androidx.room.TypeConverter
import com.uifinance.project291.data.local.entity.BudgetPeriod
import com.uifinance.project291.data.local.entity.Recurrence
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
    fun fromBudgetPeriod(value: String): BudgetPeriod {
        return BudgetPeriod.valueOf(value)
    }

    @TypeConverter
    fun budgetPeriodToString(period: BudgetPeriod): String {
        return period.name
    }

    @TypeConverter
    fun fromRecurrence(value: String): Recurrence {
        return Recurrence.valueOf(value)
    }

    @TypeConverter
    fun recurrenceToString(recurrence: Recurrence): String {
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
}
