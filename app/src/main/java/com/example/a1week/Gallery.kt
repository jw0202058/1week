package com.example.a1week

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

class Gallery : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(              // 생성된 뷰를 반환하여 화면에 보여 줌
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        val gv = view.findViewById<GridView>(R.id.gridView)
        val gAdapter = MyGridAdapter(requireContext())
        gv.adapter = gAdapter

        return view
    }

        inner class MyGridAdapter(private val context: Context) : BaseAdapter() {

        private val picID = arrayListOf<Int>(           // 사진 받아오기
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
            )

        private val selectedImages = arrayListOf<Int>()

        override fun getCount(): Int {
            return picID.size
        }

        override fun getItem(i: Int): Any {
            return 0
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder")         // 받아온 사진을 다른 뷰에 할당하여 화면에 띄우기
        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            val imageView = ImageView(context)

            imageView.layoutParams = ViewGroup.LayoutParams(200, 300)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageResource(picID[i])

            imageView.setOnClickListener {      // imageView 누르면 나타나는 일 (다이얼로그가 뜸)
                val dialogView = View.inflate(requireContext(), R.layout.dialog, null)          // 뜨는 창
                val dlg = AlertDialog.Builder(requireContext())             // 길게 쓰기 귀찮으니까 그냥 dlg로 선언해 줌
                val ivPic = dialogView.findViewById<ImageView>(R.id.ivPic)  // 이미지 받아 오기
                ivPic.setImageResource(picID[i])            // 다이얼로그의 이미지뷰 id인 ivPic에 위에서 받아온 사진 보여주기
                dlg.setTitle("큰 이미지")                    //
                dlg.setView(dialogView)
                dlg.setNegativeButton("닫기", null)
                dlg.show()
            }

            imageView.setOnLongClickListener{
                showImageDialog(i)
                true
            }

            return imageView
        }

            private fun showImageDialog(position: Int) {        // 사진 삭제
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("알림")
                .setMessage("이 사진을 삭제하시겠습니까?")
                .setNegativeButton("아니오", null)

                .setPositiveButton("예") { _, _ ->
                    removeImage(position)               // 밑에 함수에서 구현
                }
                .create()

            dialog.show()
        }
        private fun removeImage(position: Int) {            // 사진 삭제 후 완료 toast
            val deletedImageId = picID[position]
            selectedImages.remove(deletedImageId)
            picID.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(requireContext(), "사진이 삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}