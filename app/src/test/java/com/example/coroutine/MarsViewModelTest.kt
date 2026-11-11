package com.example.coroutine

import com.example.coroutine.data.PhotosRepository
import com.example.coroutine.network.Photo
import com.example.coroutine.rules.TestDispatcherRule
import com.example.coroutine.ui.MarsViewModel
import com.example.coroutine.ui.PhotosUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * 성공 경로 테스트용 FakeRepository
 */
class FakePhotosRepository(
    private val fakePhotos: List<Photo> = fakePhotoList
) : PhotosRepository {
    override suspend fun getPhotos(): List<Photo> = fakePhotos
}

/**
 * 실패 경로 테스트용 FakeErrorRepository
 */
class FakeErrorPhotosRepository(
    private val errorMessage: String = "네트워크 오류가 발생했습니다."
) : PhotosRepository {
    override suspend fun getPhotos(): List<Photo> {
        throw RuntimeException(errorMessage)
    }
}

private val fakePhotoList = listOf(
    Photo(id = "1", imgSrc = "https://example.com/photo1.jpg"),
    Photo(id = "2", imgSrc = "https://example.com/photo2.jpg")
)

@OptIn(ExperimentalCoroutinesApi::class)
class MarsViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun marsViewModel_getPhotos_verifySuccessState() = runTest {
        // Given: FakeRepository 생성
        val repository = FakePhotosRepository(fakePhotoList)

        // When: MarsViewModel 생성 및 getPhotos 실행
        val viewModel = MarsViewModel(photosRepository = repository)

        // Then: uiState가 Success 상태인지 assertEquals로 검증
        val expectedState = PhotosUiState.Success(fakePhotoList)
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun marsViewModel_getPhotos_verifyErrorState() = runTest {
        // Given: 예외를 던지는 FakeErrorRepository 생성
        val errorMessage = "네트워크 오류가 발생했습니다."
        val repository = FakeErrorPhotosRepository(errorMessage)

        // When: MarsViewModel 생성
        val viewModel = MarsViewModel(photosRepository = repository)

        // Then: uiState가 Error 상태인지 assertEquals로 검증
        val expectedState = PhotosUiState.Error(errorMessage)
        assertEquals(expectedState, viewModel.uiState.value)
    }
}
