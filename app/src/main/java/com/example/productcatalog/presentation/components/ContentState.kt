package com.example.productcatalog.presentation.components

import com.example.productcatalog.domain.model.AppError

sealed interface ContentState<out T> {
    data object Loading : ContentState<Nothing>
    data object Empty : ContentState<Nothing>
    data class Success<T>(val data: T) : ContentState<T>
    data class Error(val error: AppError) : ContentState<Nothing>
}
