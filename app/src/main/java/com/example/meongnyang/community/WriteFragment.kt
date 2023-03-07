package com.example.meongnyang.community

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
import com.example.meongnyang.databinding.CommuFragmentWriteBinding
import com.example.meongnyang.model.GetPosts
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.PetModel
import com.example.meongnyang.model.PostModel
import com.example.meongnyang.mypage.KeyboardVisibilityUtils
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
import java.io.OutputStream

class WriteFragment : Fragment() {
    private lateinit var binding: CommuFragmentWriteBinding
    private lateinit var bitmap: Bitmap
    private lateinit var data: PostModel
    var type = 0
    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()
    var category = 0
    var fileName = ""
    var fileUrl = ""
    var strCategory = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.commu_fragment_write,
            container,
            false
        )

        val retrofit = RetrofitApi.create() // 서버와 통신 연결

        // 스피너 설정
        val categoryAdapter =
            context?.let {
                ArrayAdapter.createFromResource(it, R.array.category_array, android.R.layout.simple_spinner_dropdown_item)
            }
        categoryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectCategory.adapter = categoryAdapter // 어댑터와 연결

        binding.imgBtn.setOnClickListener {
            checkPermission {
                openGallery()
            }
        }

        // 작성 완료 버튼 누르면 입력한 제목, 내용, 카테고리, 사진 (있으면 링크, 없으면 값?) 넘겨주기
        binding.finishBtn.setOnClickListener {
            category = binding.selectCategory.selectedItemPosition
            var title = binding.postTitle.editText?.text.toString()
            var contents = binding.postContent.editText?.text.toString()
            var img = fileUrl

            fbFirestore!!.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    var id = documentsSnapshot.toObject<Id>()!!

                    retrofit.getPet(id.conimalId!!).enqueue(object: Callback<PetModel> {
                        override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                            type = response.body()!!.type
                            data = PostModel(category, type, title, contents, img)

                            retrofit.createPost(data, id.memberId!!).enqueue(object: Callback<GetPosts> {
                                override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                                    if (!response.body().toString().isEmpty()) {
                                        Log.d("result", response.body().toString())
                                    }
                                }
                                override fun onFailure(call: Call<GetPosts>, t: Throwable) {
                                    Log.d("error", "fail")
                                    Log.d("error", t.message.toString())
                                }
                            })
                        }

                        override fun onFailure(call: Call<PetModel>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                    (activity as NaviActivity).replace(CommuFragment())
                }

        }

        return binding.root
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
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, currentImageUrl)

                fileName = "MEONGNYANG_" + System.currentTimeMillis() + ".png"

                try {
                    var category = binding.selectCategory.selectedItemPosition
                    if (category == 1) {
                        strCategory  = "free"
                    } else if (category == 2) {
                        strCategory = "question"
                    } else if (category == 3) {
                        strCategory = "boast"
                    }

                    // 이미지 s3에 올리기
                    uploadImage(fileName, bitmapToFile(bitmap, fileName), strCategory)
                    fileUrl = "https://meongnyang.s3.ap-northeast-2.amazonaws.com/${strCategory}/${fileName}"
                    // 게시물에 내가 올린 이미지 보여주기
                    binding.postImg.setImageBitmap(bitmap)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("profile", e.printStackTrace().toString())
                }
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }
    }

    private fun uploadImage(fileName: String, file: File, category: String) {
        val awsCredentials = BasicAWSCredentials(getString(R.string.access_key), getString(R.string.secret_key))
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(activity?.applicationContext).build()
        TransferNetworkLossHandler.getInstance(activity?.applicationContext)

        val uploadObserver = transferUtility.upload("meongnyang/${category}", fileName, file)

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

    private fun bitmapToFile(bitmap: Bitmap, fileName: String): File {
        //var file = File(path)
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
