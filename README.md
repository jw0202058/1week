
# 몰입캠프 1주차 - 탭 구조를 활용한 안드로이드 앱 제작
* 세 개의 TAB을 가지고 있는 안드로이드 앱

## 프로젝트 소개 - Just TODO
간단한 TODO List 어플리케이션 입니다.    


## 팀원

- [@이정원](https://github.com/jw0202058) 고려대학교 컴퓨터학과 21학번
- [@모지훈](https://github.com/Morivy42) KAIST 전산학부 18학번


## 개발 환경

* OS : Android
* Language : Kotlin
* IDE : Android Studio
* Target Device : Galaxy S7

## TAB 1 : 연락처
### ![실행 화면](https://github.com/jw0202058/1week/blob/master/asset/%EC%97%B0%EB%9D%BD%EC%B2%98%20demo.gif)


### 상세 정보

- 연락처 정보 (프로필 이미지, 이름, 전화번호) 불러오기
```
@SuppressLint("Range")
    private fun loadContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver

        // Query the contacts
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.let {
            if (it.count > 0) {
                val contacts = mutableListOf<Contact>()

                while (it.moveToNext()) {
                    val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val photoUri = getContactPhotoUri(id)

                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    phoneCursor?.let { phoneCursor ->
                        while (phoneCursor.moveToNext()) {
                            val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val contact = Contact(id, name, phoneNumber, photoUri)
                            contacts.add(contact)
                        }

                        phoneCursor.close()
                    }
                }

                adapter.setData(contacts)
            }

            it.close()

        }
    }
```
Intent와 Content resolver로 이미지, 이름 전화번호 정보를 가져온다.

- 전화 앱 연결: 전화 버튼을 누르면 전화 앱으로 연결하고 번호 입력
```
// ContactAdapter.kt
...
override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.itemView.apply {
            // Bind data to views within the item layout
            findViewById<TextView>(R.id.txtName).text = contact.name
            findViewById<TextView>(R.id.txtPhoneNumber).text = contact.phoneNumber
            if (contact.photoUri == null) {
                findViewById<ImageView>(R.id.contact_photo_imageview).setImageResource(R.drawable.baseline_person_24)
            } else {
                findViewById<ImageView>(R.id.contact_photo_imageview).setImageURI(contact.photoUri)
            }
        }
    }
...

// ContactFragment.kt
...
    override fun onDialButtonClick(contact: Contact, position: Int) {
        Toast.makeText(context, "Dial to ${contact.name}", Toast.LENGTH_SHORT).show()
        val phoneNumber = contact.phoneNumber
        val phoneNumUri = Uri.parse("tel:$phoneNumber")
        val dialIntent = Intent(Intent.ACTION_DIAL, phoneNumUri)
        startActivity(dialIntent)
    }
...

```
- 메시지 앱 연결: 메시지 버튼을 누르면 메시지 앱으로 연결하고 번호 입력
```
// ContactFragment.kt
...
    override fun onMsgButtonClick(contact: Contact, position: Int) {
        val phoneNumber = contact.phoneNumber
        val phoneNumUri = Uri.parse("smsto:$phoneNumber")
        val msgIntent = Intent(Intent.ACTION_SENDTO, phoneNumUri)
        msgIntent.putExtra("sms_body", "Here goes your message...")
        startActivity(msgIntent)
    }
...

```

## TAB 2 : 갤러리

### ![실행 화면](https://github.com/jw0202058/1week/blob/master/asset/%EA%B0%A4%EB%9F%AC%EB%A6%AC%20demo.gif)


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
  - removeImage(position) 함수가 position 매개변수로 전달된 위치에 있는 이미지를 삭제
    ``` kotlin
            private fun removeImage(position: Int) {
            val bitmap = picID[position]
            picID.removeAt(position)
            notifyDataSetChanged()
            deleteImageFromStorage(bitmap)
            Toast.makeText(requireContext(), "사진이 삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
    ```

## TAB 3 : 투두리스트

### ![실행 화면](https://github.com/jw0202058/1week/blob/master/asset/%ED%95%A0%EC%9D%BC%20demo.gif)

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
