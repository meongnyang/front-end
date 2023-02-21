package com.example.meongnyang.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
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
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class ModifyFragment : Fragment() {
    private lateinit var editName: EditText
    private lateinit var saveBtn: Button
    private lateinit var profile: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var editText: TextView
    var memberId = 0
    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mypage_fragment_modify, container, false)
        val retrofit = RetrofitApi.create()

        editName = view.findViewById(R.id.editName)
        saveBtn = view.findViewById(R.id.saveBtn)
        profile = view.findViewById(R.id.profile)
        editText = view.findViewById(R.id.editText)

        checkPermission {
            // 갤러리에서 선택 클릭했을 때
            profile.setOnClickListener {
                openGallery()
            }
            editText.setOnClickListener {
                openGallery()
            }
        }

        saveBtn.setOnClickListener {
            fbFirestore!!.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    var id = documentsSnapshot.toObject<Id>()!!

                    var nickname = editName.text.toString()
                    retrofit.updateNickname(id.memberId!!, Nickname(nickname)).enqueue(object : Callback<UserModel> {
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
        }
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
                val fileName = "MEONGNYANG_" + System.currentTimeMillis() + ".png"
                val inputStream = activity?.contentResolver?.openInputStream(currentImageUrl!!)
                var file = File.createTempFile("image", fileName)
                val outStream = FileOutputStream(file)
                outStream.write(inputStream!!.readBytes())
                try {
                    // 이미지 s3에 올리기
                    uploadImage(fileName, file)
                    // 올리고 마이페이지로 돌아가기
                    val bundle = Bundle()
                    bundle.putString("userImg", "https://meongnyang.s3.ap-northeast-2.amazonaws.com/person/${fileName}")
                    val myFragment = MyFragment()
                    myFragment.arguments = bundle
                    (activity as NaviActivity).replace(myFragment)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("profile", e.printStackTrace().toString())
                }
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        if (contentUri.path!!.startsWith("/storage")) {
            return contentUri.path
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":")[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns._ID + " = " + id
        val cursor: Cursor? = activity?.contentResolver?.query(
            MediaStore.Files.getContentUri("external"),
            columns,
            selection,
            null,
            null
        )
        try {
            val columnIndex: Int = cursor!!.getColumnIndex(columns[0])
            if (cursor!!.moveToFirst()) {
                return cursor?.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
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
                    //Toast.makeText(this@ModifyFragment, "업로드 완료!", Toast.LENGTH_SHORT).show()
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
}