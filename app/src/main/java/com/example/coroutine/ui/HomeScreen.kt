package com.example.coroutine.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.coroutine.R
import com.example.coroutine.network.Photo

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    photosViewModel: PhotosViewModel = viewModel()
) {
    val uiState by photosViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is PhotosUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PhotosUiState.Success -> PhotosGridScreen(
            photos = state.photos,
            modifier = modifier.fillMaxSize()
        )
        is PhotosUiState.Error -> ErrorScreen(
            retryAction = { photosViewModel.getPhotos() },
            errorMessage = state.message,
            modifier = modifier.fillMaxSize()
        )
    }
}

/**
 * 로딩 상태 화면
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "사진 목록을 불러오는 중입니다...",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * 에러 상태 화면
 */
@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "오류가 발생했습니다.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = retryAction) {
            Text("다시 시도")
        }
    }
}

/**
 * 성공 상태: Coil 3 AsyncImage와 LazyVerticalGrid를 사용하여 그리드로 사진 표시
 */
@Composable
fun PhotosGridScreen(
    photos: List<Photo>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "총 ${photos.size}개의 사진을 로드했습니다.",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = photos, key = { photo -> photo.id }) { photo ->
                PhotoCard(photo = photo)
            }
        }
    }
}

@Composable
fun PhotoCard(
    photo: Photo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = photo.imgSrc,
            contentDescription = "사진 아이디: ${photo.id}",
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.ic_broken_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}
