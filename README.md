
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

### 실행 화면

### 상세 정보
- onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?: 이 함수는 프래그먼트의 레이아웃을 인플레이트하고 루트 뷰를 반환합니다. R.layout.fragment_gallery 레이아웃을 인플레이트한 후, GridView와 어댑터를 초기화하고 FloatingActionButton의 클릭 이벤트를 설정합니다. 그리고 이미지들을 내부 저장소에서 로드하여 GridView에 표시합니다.

- addImageToStorage(bitmap: Bitmap): 이 함수는 주어진 비트맵 이미지를 내부 저장소에 추가합니다. 주어진 비트맵을 PNG 형식으로 압축하여 내부 저장소에 저장합니다.

- loadImagesFromStorage(): 이 함수는 내부 저장소에 저장된 이미지들을 로드합니다. 내부 저장소에 있는 이미지 파일들을 가져와서 비트맵으로 디코딩한 후, 어댑터에 이미지를 추가합니다.

- checkPermission(): Boolean: 이 함수는 외부 저장소 읽기 권한이 있는지 확인합니다. 권한이 허용되어 있는 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.

- requestPermission(): 이 함수는 외부 저장소 읽기 권한을 요청합니다. REQUEST_CODE_PERMISSION 요청 코드로 권한 요청을 수행합니다.

- onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray): 이 함수는 권한 요청 결과를 처리합니다. 요청 코드와 권한 승인 결과를 확인하여 권한이 승인된 경우 갤러리를 엽니다. 그렇지 않은 경우 "Permission Denied" 메시지를 표시합니다.

- openGallery(): 이 함수는 갤러리 앱을 엽니다. 이미지 선택을 위해 ACTION_PICK 인텐트를 사용하여 외부 저장소의 이미지를 선택할 수 있는 액티비티를 호출합니다.

- onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): 이 함수는 액티비티에서 결과를 받아옵니다. 이미지 선택이 성공적으로 이루어진 경우, 선택한 이미지의 URI를 가져와서 비트맵으로 디코딩합니다. 그 후 어댑터에 이미지를 추가하고, 추가된 이미지를 내부 저장소에 저장합니다.

- decodeUriToBitmap(context: Context, uri: Uri): Bitmap?: 이 함수는 주어진 URI를 사용하여 비트맵 이미지를 디코딩합니다. 주어진 URI를 기반으로 컨텐트 리졸버를 사용하여 이미지의 InputStream을 가져와서 비트맵으로 디코딩합니다.

- MyGridAdapter(context: Context) : BaseAdapter(): 내부 클래스인 MyGridAdapter는 GridView의 어댑터로 사용됩니다. BaseAdapter를 상속하여 구현되었습니다.

- getCount(): 어댑터에 있는 항목의 개수를 반환합니다.

- getItem(i: Int): Any: 주어진 인덱스의 항목을 반환합니다. 여기서는 0을 반환합니다.

- getItemId(i: Int): Long: 주어진 인덱스의 항목 ID를 반환합니다. 여기서는 0을 반환합니다.

- getView(i: Int, view: View?, viewGroup: ViewGroup): View: 주어진 인덱스의 항목에 대한 뷰를 반환합니다. ImageView를 생성하고 비트맵을 설정합니다. 이미지를 클릭할 때는 해당 이미지를 크게 보여주는 다이얼로그를 표시합니다. 이미지를 길게 클릭할 때는 삭제 여부를 묻는 다이얼로그를 표시하고, 확인을 누를 경우 이미지를 삭제합니다.

- showImageDialog(position: Int): 주어진 인덱스의 이미지에 대한 삭제 여부 다이얼로그를 표시합니다. 확인을 누를 경우 해당 이미지를 삭제합니다.

- removeImage(position: Int): 주어진 인덱스의 이미지를 삭제하고 어댑터를 업데이트합니다. 동시에 내부 저장소에서 해당 이미지를 삭제합니다.

- addImage(bitmap: Bitmap): 비트맵 이미지를 어댑터에 추가하고 업데이트합니다. 새로운 이미지는 리스트의 맨 앞에 추가됩니다.


## TAB 3 : 투두리스트

### 실행 화면

### 상세 정보
- onBindViewHolder(todoViewHolder: TodoViewHolder, position: Int): 이 함수는 RecyclerView에서 각 아이템을 화면에 표시할 때 호출됩니다. onBindViewHolder 함수는 아이템의 위치(position)에 해당하는 데이터를 가져와서 ViewHolder에 바인딩하는 역할을 합니다.

- showEditDialog(context: Context, todo: Todo, onConfirm: (updatedText: String) -> Unit): 이 함수는 길게 클릭한 할 일의 수정 다이얼로그를 표시하는 기능을 담당합니다. 다이얼로그에서 할 일을 수정하고 확인 버튼을 누르면 onConfirm 콜백 함수가 호출됩니다. 수정된 텍스트는 이 콜백 함수를 통해 전달되고, 해당 할 일의 텍스트를 업데이트하고 화면을 갱신합니다.
  
- onClickDeleteIcon.invoke(todo): 이 부분은 할 일을 삭제하는 기능을 처리합니다. deleteImage ImageView가 클릭되었을 때 실행되며, onClickDeleteIcon 함수를 호출하고 삭제할 할 일(todo)을 전달합니다. onClickDeleteIcon 함수는 외부에서 TodoAdapter 객체를 생성할 때 전달된 함수로, 할 일을 삭제하는 로직을 처리합니다.

- addTask(): 이 함수는 새로운 할 일을 추가하는 기능을 담당합니다. addButton이 클릭되었을 때 실행되며, 입력된 텍스트를 가져와서 새로운 Todo 객체를 생성합니다. 그리고 해당 객체를 data 리스트에 추가하고 RecyclerView의 어댑터에 변경 내용을 알리고 화면을 갱신합니다. 마지막으로 데이터를 내부 저장소에 저장합니다.

- deleteTask(todo: Todo): 이 함수는 할 일을 삭제하는 기능을 담당합니다. 특정 할 일(todo)을 인자로 받으며, 해당 할 일을 data 리스트에서 제거하고 RecyclerView의 어댑터에 변경 내용을 알리고 화면을 갱신합니다. 마지막으로 데이터를 내부 저장소에 저장합니다.

- saveData(todoList: List<Todo>): 이 함수는 Todo 리스트를 내부 저장소에 저장하는 기능을 담당합니다. todoList 인자로 전달된 Todo 리스트를 JSON 형태로 변환하고 내부 저장소에 저장합니다.

- loadData(): 이 함수는 이전에 저장한 데이터를 내부 저장소에서 불러오는 기능을 담당합니다. 내부 저장소에서 "todo_list"라는 키로 저장된 JSON 데이터를 가져와서 다시 Todo 객체로 변환한 후 data 리스트에 추가합니다. 이 함수는 앱이 처음 실행되거나 데이터를 불러와야 할 때 호출됩니다.

- onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View: 이 함수는 프래그먼트의 레이아웃을 인플레이트하고 루트 뷰를 반환합니다. inflater와 container를 사용하여 FragmentTodoBinding 레이아웃을 인플레이트한 후에 루트 뷰를 반환합니다.

- onViewCreated(view: View, savedInstanceState: Bundle?): 이 함수는 프래그먼트의 뷰가 생성된 후에 호출됩니다. 여기에서는 데이터를 초기화하고, 이전에 로드되지 않았다면 데이터를 로드합니다.
