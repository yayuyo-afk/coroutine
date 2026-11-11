package com.example.coroutine.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutine.data.NetworkPhotosRepository
import com.example.coroutine.data.PhotosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarsViewModel(
    private val photosRepository: PhotosRepository = NetworkPhotosRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<PhotosUiState>(PhotosUiState.Loading)
    val uiState: StateFlow<PhotosUiState> = _uiState.asStateFlow()

    init {
        getPhotos()
    }

    fun getPhotos() {
        viewModelScope.launch {
            _uiState.value = PhotosUiState.Loading
            try {
                val list = photosRepository.getPhotos()
                _uiState.value = PhotosUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = PhotosUiState.Error(
                    e.localizedMessage ?: "네트워크 연결 또는 데이터를 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }
}

typealias PhotosViewModel = MarsViewModel
