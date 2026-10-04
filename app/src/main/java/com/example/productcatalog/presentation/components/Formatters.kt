package com.example.productcatalog.presentation.components

import androidx.annotation.StringRes
import com.example.productcatalog.R
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.domain.model.Money
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Money.formatted(): String = NumberFormat.getCurrencyInstance(Locale.US).apply {
    currency = Currency.getInstance("USD")
}.format(BigDecimal.valueOf(cents, 2))

fun Double.formattedRating(): String = String.format(Locale.getDefault(), "%.1f", this)

@StringRes
fun AppError.messageResource(): Int = when (this) {
    AppError.NoInternet -> R.string.error_no_internet
    AppError.Timeout -> R.string.error_timeout
    AppError.Server -> R.string.error_server
    AppError.NotFound -> R.string.error_not_found
    AppError.RequestRejected -> R.string.error_rejected
    AppError.InvalidResponse -> R.string.error_invalid
    AppError.Storage -> R.string.error_storage
    AppError.QuantityLimit -> R.string.error_limit
    AppError.Unknown -> R.string.error_unknown
}
