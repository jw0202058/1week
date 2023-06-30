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
import androidx.fragment.app.Fragment

class Gallery : Fragment() {

    override fun onCreateView(
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
        private val picID = arrayOf<Int>(
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
            R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1, R.drawable.img1,
        )

        override fun getCount(): Int {
            return picID.size
        }

        override fun getItem(i: Int): Any {
            return 0
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder")
        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            val imageView = ImageView(context)
            imageView.layoutParams = ViewGroup.LayoutParams(200, 300)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageResource(picID[i])

            imageView.setOnClickListener {
                val dialogView = View.inflate(requireContext(), R.layout.dialog, null)
                val dlg = AlertDialog.Builder(requireContext())
                val ivPic = dialogView.findViewById<ImageView>(R.id.ivPic)
                ivPic.setImageResource(picID[i])
                dlg.setTitle("큰 이미지")
                dlg.setView(dialogView)
                dlg.setNegativeButton("닫기", null)
                dlg.show()
            }

            return imageView
        }
    }
}