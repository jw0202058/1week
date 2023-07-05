
# 몰입캠프 1주차 - 탭 구조를 활용한 안드로이드 앱 제작

* 세 개의 TAB을 가지고 있는 안드로이드 앱


## 팀원

- [@이정원](https://github.com/jw0202058) 고려대학교 컴퓨터학과 21학번
- [@모지훈](https://github.com/Morivy42) KAIST 전산학부 18학번


## 개발 환경

* OS : Android
* Language : Kotlin
* IDE : Android Studio
* Target Device : Galaxy S7

## TAB 1 : 연락처
### 실행 화면


### 상세 정보

- 연락처 정보 (프로필 이미지, 이름, 전화번호) 불러오기
```

```
- 전화 앱 연결: 전화 버튼을 누르면 전화 앱으로 연결하고 번호 입력
```

```
- 메시지 앱 연결: 메시지 버튼을 누르면 메시지 앱으로 연결하고 번호 입력
```

```

## TAB 2 : 갤러리

### 실행 화면

### 상세 정보
- 사용자 갤러리에 접근해서 사진 불러와서 추가
  - 사진을 추가하기 위해 fabMain (FloatingActionButton) 클릭시, openGallery() 함수 호출
  - 이 함수는 갤러리에서 이미지를 선택하는 intent를 실행
  ``` kotlin
      private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }
  ```
    - 사용자가 이미지를 선택하면 onActivityResult() 메서드가 호출되고 선택한 이미지를 Bitmap으로 변환
  ``` kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                val bitmap = decodeUriToBitmap(requireContext(), selectedImageUri)
                if (bitmap != null) {
                    gAdapter.addImage(bitmap)
                    addImageToStorage(bitmap) // Save image to internal storage
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
  ```
    - gAdapter.addImage(bitmap) 함수를 호출하여 이미지를 GridView에 추가
<br>
- 애플리케이션 내부 저장소에 사진 저장
  - addImageToStorage(bitmap) 함수를 호출하여 이미지를 내부 저장소에 저장
  ``` kotlin
      private fun addImageToStorage(bitmap: Bitmap) {
        try {
            val imageFileName = "image_${System.currentTimeMillis()}.png"
            val fileOutputStream = requireContext().openFileOutput(imageFileName, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Failed to save image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
  ```
- 사진 삭제
```


```

## TAB 3 : 투두리스트

### 실행 화면

### 상세 정보

- 새 할 일 추가


- 할 일 수정
  - setOnLongClickListener
```

```
- 완료 표시
  - imageView, visible
```

```
- 할 일 삭제
  - imageView, setClickListener
```

```
- 애플리케이션 내부저장소에 저장
  - sharedpreferences
```

```
