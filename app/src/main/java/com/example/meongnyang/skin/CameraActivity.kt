package com.example.meongnyang.skin

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.meongnyang.databinding.SkinActivityCameraBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PictureCallback {

        private lateinit var binding: SkinActivityCameraBinding

        private var surfaceHolder: SurfaceHolder? = null
        private var camera: Camera? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = SkinActivityCameraBinding.inflate(layoutInflater)
            setContentView(binding.root)

            checkPermission {
                setupSurfaceHolder()
                startCamera()
            }
        }

    @SuppressLint("MissingPermission")
    private fun checkPermission(logic: () -> Unit) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                // 권한이 허용되었을 때
                override fun onPermissionGranted() {
                    logic()
                }
                // 권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@CameraActivity, "권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            })
            .setRationaleMessage("카메라 권한이 필요한 서비스입니다.")
            .setDeniedMessage("카메라 권한을 허용해 주세요! [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                CAMERA)
            .check()
    }

        private fun setupSurfaceHolder() {
            binding.surfaceView.visibility = View.VISIBLE
            surfaceHolder = binding.surfaceView.holder
            binding.surfaceView.holder.addCallback(this)
            setBtnClick()
        }

        private fun setBtnClick() {
            binding.showResultBtn.setOnClickListener { captureImage() }
        }

        private fun captureImage() {
            if (camera != null) {
                camera!!.takePicture(null, null, this)
            }
        }

        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            startCamera()
        }

        private fun startCamera() {
            camera = Camera.open()
            camera!!.setDisplayOrientation(90)
            try {
                camera!!.setPreviewDisplay(surfaceHolder)
                camera!!.startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
            resetCamera()
        }

        private fun resetCamera() {
            if (surfaceHolder!!.surface == null) {
                // Return if preview surface does not exist
                return
            }

            // Stop if preview surface is already running.
            camera!!.stopPreview()
            try {
                // Set preview display
                camera!!.setPreviewDisplay(surfaceHolder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Start the camera preview...
            camera!!.startPreview()
        }

        override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
            releaseCamera()
        }

        private fun releaseCamera() {
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }

        override fun onPictureTaken(bytes: ByteArray, camera: Camera) {
            showImage(bytes)
            saveImage(bytes)
            resetCamera()
        }

        private fun saveImage(bytes: ByteArray) {
            val outStream: FileOutputStream
            try {
                // 이미지 저장하기
                val fileName = "MEONGNYANG_" + System.currentTimeMillis() + ".jpg"
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    fileName
                )
                outStream = FileOutputStream(file)
                outStream.write(bytes)
                outStream.close()
                Toast.makeText(this@CameraActivity, "저장 완료! $fileName", Toast.LENGTH_SHORT).show()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun showImage(bytes: ByteArray) {
            // 이미지 위에 띄워보기 일단
            val options = BitmapFactory.Options()
            options.inSampleSize = 2 // 1/2 사이즈로 보여주기
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val width = bitmap.width
            val height = bitmap.height
            val newWidth = 224
            val newHeight = 224
            val scaleWidth = (newWidth.toFloat()) / width
            val scaleHeight = (newHeight.toFloat()) / height

            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            matrix.postRotate(90f)

            val resizedBitmap: Bitmap = Bitmap.createBitmap(bitmap, 0, 0,  width, height, matrix, true)
            val bmd: BitmapDrawable = BitmapDrawable(resizedBitmap)

            binding.picView.setImageDrawable(bmd)
            camera!!.startPreview()

        }

        companion object {
            const val REQUEST_CODE = 100
        }
}