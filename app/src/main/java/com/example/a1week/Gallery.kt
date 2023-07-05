package com.example.a1week

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Gallery : Fragment() {
    private val REQUEST_CODE_PERMISSION = 123
    private val REQUEST_CODE_PICK_IMAGE = 456

    private lateinit var gv: GridView
    private lateinit var gAdapter: MyGridAdapter

    private lateinit var fabMain: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        gv = view.findViewById<GridView>(R.id.gridView)
        gAdapter = MyGridAdapter(requireContext())
        gv.adapter = gAdapter

        fabMain = view.findViewById<FloatingActionButton>(R.id.fabMain)
        fabMain.setOnClickListener {
            if (checkPermission()) {
                openGallery()
            } else {
                requestPermission()
            }
        }
        loadImagesFromStorage()

        return view
    }

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

    private fun loadImagesFromStorage() {
        try {
            val imageFiles = requireContext().filesDir.listFiles()
            if (imageFiles != null) {
                for (imageFile in imageFiles) {
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                    if (bitmap != null) {
                        gAdapter.addImage(bitmap)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Failed to load images",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

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

    private fun decodeUriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    inner class MyGridAdapter(private val context: Context) : BaseAdapter() {

        private val picID = arrayListOf<Bitmap>()

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
            imageView.setImageBitmap(picID[i])

            imageView.setOnClickListener {
                val dialogView = View.inflate(requireContext(), R.layout.dialog, null)
                val dlg = AlertDialog.Builder(requireContext())
                val ivPic = dialogView.findViewById<ImageView>(R.id.ivPic)
                ivPic.setImageBitmap(picID[i])
                dlg.setTitle("큰 이미지")
                dlg.setView(dialogView)
                dlg.setNegativeButton("닫기", null)
                dlg.show()
            }

            imageView.setOnLongClickListener {
                showImageDialog(i)
                true
            }

            return imageView
        }

        private fun showImageDialog(position: Int) {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("알림")
                .setMessage("이 사진을 삭제하시겠습니까?")
                .setNegativeButton("아니오", null)
                .setPositiveButton("예") { _, _ ->
                    removeImage(position)
                }
                .create()

            dialog.show()
        }

        private fun removeImage(position: Int) {
            val bitmap = picID[position]
            picID.removeAt(position)
            notifyDataSetChanged()
            deleteImageFromStorage(bitmap)
            Toast.makeText(requireContext(), "사진이 삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
        private fun deleteImageFromStorage(bitmap: Bitmap) {
            try {
                val imageFiles = requireContext().filesDir.listFiles()
                if (imageFiles != null) {
                    for (imageFile in imageFiles) {
                        val storedBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                        if (storedBitmap != null && bitmap.sameAs(storedBitmap)) {
                            imageFile.delete()
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Failed to delete image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        fun addImage(bitmap: Bitmap) {
            picID.add(0, bitmap)
            notifyDataSetChanged()
        }
    }
}