package com.example.coroutine.data

import com.example.coroutine.network.Photo
import com.example.coroutine.network.PhotosApi
import com.example.coroutine.network.PhotosApiService

interface PhotosRepository {
    suspend fun getPhotos(): List<Photo>
}

class NetworkPhotosRepository(
    private val photosApiService: PhotosApiService = PhotosApi.retrofitService
) : PhotosRepository {
    override suspend fun getPhotos(): List<Photo> = photosApiService.getPhotos()
}
