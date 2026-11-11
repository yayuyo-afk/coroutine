# Android Coroutine & Mars Photos App

Jetpack Compose, ViewModel, Retrofit, Kotlinx Serialization, Coil 3, 그리고 코루틴 단위 테스트를 적용한 안드로이드 사진 목록 앱입니다.

---

## 📌 어떤 프롬프트로 무엇을 만들었는지

### 1. 메인 세이프 코루틴 비동기 작업 및 UI 상태 표현
* **사용된 프롬프트:**
  > "Jetpack Compose와 ViewModel에서 viewModelScope.launch로 2초 걸리는 가짜 작업(delay)을 실행하고, 진행 중에는 로딩 스피너, 끝나면 결과 텍스트를 보여줘. 그 작업을 suspend 함수로 분리하고 withContext(Dispatchers.IO)로 감싸서 main-safe하게 만들어줘."
* **구현된 기능:**
  * `viewModelScope.launch` 및 `StateFlow` 기반의 UI 상태 관리
  * `withContext(Dispatchers.IO)`를 적용하여 메인 스레드를 블록하지 않는 main-safe `suspend` 함수 구현
  * Jetpack Compose의 `CircularProgressIndicator` 및 상태 기반 UI 표현

---

### 2. Retrofit & Kotlinx Serialization을 이용한 네트워크 데이터 로딩
* **사용된 프롬프트:**
  > "Retrofit과 kotlinx-serialization으로 https 주소의 photos 엔드포인트에서 JSON 목록을 받아오는 서비스와 @Serializable 데이터 모델을 만들어줘. suspend fun getPhotos(): List<Photo> 형태로. ViewModel에서 viewModelScope로 호출하고, 결과를 Loading/Success/Error 상태로 나눠서 화면에 개수나 에러 메시지를 보여줘. 인터넷 권한도 추가해줘."
* **구현된 기능:**
  * `AndroidManifest.xml` 인터넷 권한 추가 (`android.permission.INTERNET`)
  * Kotlinx Serialization 기반 `@Serializable` `Photo` 데이터 모델 (`id`, `img_src`)
  * Retrofit + `converter-kotlinx-serialization` 서비스 (`PhotosApiService`) 구현
  * `PhotosUiState` (`Loading`, `Success`, `Error`) 및 `PhotosRepository` 아키텍처 구축

---

### 3. Coil 3 이미지 그리드 레이아웃 & 로딩/에러 화면 UI
* **사용된 프롬프트:**
  > "Coil 3의 AsyncImage로 photo.imgSrc URL 이미지를 LazyVerticalGrid에 표시해줘. coil-compose와 coil-network-okhttp 의존성도 알려줘. 로딩 중엔 placeholder, 실패하면 error이미지를 보여주고, 각 이미지에 접근성용 contentDescription을 넣어줘. Loading/Error화면도 만들어줘"
* **구현된 기능:**
  * Coil 3 (`coil-compose`, `coil-network-okhttp`) 의존성 설정
  * `LazyVerticalGrid`를 활용한 그리드 형태의 사진 목록 레이아웃
  * Coil 3 `AsyncImage` 적용:
    * 로딩 중: `placeholder` (`R.drawable.loading_img`)
    * 로드 실패 시: `error` (`R.drawable.ic_broken_image`)
    * 접근성 설정: `contentDescription` 추가
  * 독립된 `LoadingScreen` 및 `ErrorScreen` (재시도 버튼 포함) UI 제작

---

### 4. ViewModel 단위 테스트 (Main 디스패처 교체 & FakeRepository)
* **사용된 프롬프트:**
  > "MarsViewModel을 위한 단위테스트를 만들어줘. FakeRepository로 성공 경로를 검증하고, runTest와 TestDispatcherRule로 Main 디스패처를 교체해. success 상태가 되는지 assertEquals로 확인. 예외를 던지는 FakeErrorRepository로 실패 경로 테스트도 추가해서 uiState가 Error가 되는지 확인해줘. 필요한 test 의존성(kotlinx-coroutines-test, junit)도 알려줘."
* **구현된 기능:**
  * `kotlinx-coroutines-test` 기반 단위 테스트 환경 구축
  * `TestDispatcherRule` (`TestWatcher`)을 사용하여 `Dispatchers.Main`을 테스트 디스패처로 교체
  * 성공 경로 검증 테스트 (`FakePhotosRepository` 사용)
  * 실패 경로 검증 테스트 (`FakeErrorPhotosRepository` 예외 발생 사용)
  * `runTest`와 `assertEquals`를 사용하여 `MarsViewModel` 상태 검증

---

## 🛠 기술 스택
* **Language:** Kotlin
* **UI:** Jetpack Compose, Material 3
* **Async & State:** Kotlin Coroutines, StateFlow, ViewModel
* **Network & Serialization:** Retrofit2, Kotlinx Serialization
* **Image Loading:** Coil 3 (`coil-compose`, `coil-network-okhttp`)
* **Testing:** JUnit4, Kotlinx Coroutines Test
