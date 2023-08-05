package com.nakyung.meongnyang.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.nakyung.meongnyang.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ModifyFragment : Fragment() {
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private lateinit var editName: EditText
    private lateinit var saveBtn: Button
    private lateinit var profile: ImageView
    private lateinit var editText: TextView
    private lateinit var modifySV: ScrollView
    var memberId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mypage_fragment_modify, container, false)

        editName = view.findViewById(R.id.editName)
        saveBtn = view.findViewById(R.id.saveBtn)
        profile = view.findViewById(R.id.profile)
        editText = view.findViewById(R.id.editText)
        modifySV = view.findViewById(R.id.modify_sv)

        memberId = App.prefs.getInt("memberId", 0)

        profile.setOnClickListener {
            checkPermission {
                openGallery()
            }
        }
        editText.setOnClickListener {
            checkPermission {
                openGallery()
            }
        }

        saveBtn.setOnClickListener {
            var nickname = editName.text.toString()
            updateName(memberId, nickname)
        }

        // 키보드 올라 왔을 때 화면 가리는 것 방지하는 코드
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                modifySV.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })

        return view
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
                    Toast.makeText(context, "권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            })
            .setRationaleMessage("카메라 권한이 필요한 서비스입니다.")
            .setDeniedMessage("카메라 권한을 허용해 주세요! [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .check()
    }

    // 갤러리 열기
    private val OPEN_GALLERY = 1
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = ("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var currentImageUrl = data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, currentImageUrl)

                val fileName = "MEONGNYANG_" + System.currentTimeMillis() + ".png"

                try {
                    // 이미지 s3에 올리기
                    uploadImage(fileName, bitmapToFile(bitmap, fileName))
                    updateImg(memberId, "https://meongnyang.s3.ap-northeast-2.amazonaws.com/person/${fileName}")

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("profile", e.printStackTrace().toString())
                }
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }
    }

    private fun uploadImage(fileName: String, file: File) {
        val awsCredentials = BasicAWSCredentials(getString(R.string.access_key), getString(R.string.secret_key))
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(activity?.applicationContext).build()
        TransferNetworkLossHandler.getInstance(activity?.applicationContext)

        val uploadObserver = transferUtility.upload("meongnyang/person", fileName, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {

                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })
    }

    private fun updateImg(memberId: Int, img: String) {
        val retrofit = RetrofitApi.create()
        retrofit.updateProfile(memberId, Img(img)).enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(context, "변경 실패, 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                // 올리고 마이페이지로 돌아가기
                val bundle = Bundle()
                bundle.putString("userImg", img)
                val myFragment = MyFragment()
                myFragment.arguments = bundle
                (activity as NaviActivity).replace(myFragment)
            }
        })
    }

    private fun updateName(memberId: Int, nickname: String) {
        val retrofit = RetrofitApi.create()
        retrofit.updateNickname(memberId, Nickname(nickname)).enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(context, "변경 실패, 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val bundle = Bundle()
                bundle.putString("nickname", nickname)
                val myFragment = MyFragment()
                myFragment.arguments = bundle
                (activity as NaviActivity).replace(myFragment)
            }
        })
    }

    private fun bitmapToFile(bitmap: Bitmap, fileName: String): File {
        var file =  File.createTempFile("image", fileName)
        var out: OutputStream? = null

        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
        } finally {
            out?.close()
        }
        return file
    }
}