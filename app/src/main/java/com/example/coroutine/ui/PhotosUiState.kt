package com.example.coroutine.ui

import com.example.coroutine.network.Photo

sealed interface PhotosUiState {
    data class Success(val photos: List<Photo>) : PhotosUiState
    data class Error(val message: String) : PhotosUiState
    data object Loading : PhotosUiState
}
