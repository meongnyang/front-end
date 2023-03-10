package com.example.meongnyang.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.example.meongnyang.databinding.LoginActivityEnrollBinding
import com.example.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*

class EnrollActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityEnrollBinding
    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()
    var memberId = 0
    var conimalId = 0
    var birthString = ""
    var adoptString = ""
    var birth = ""
    var adopt = ""
    var gender = ""
    var neutering = 0
    var species = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityEnrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = RetrofitApi.create()

        /*fbFirestore!!.collection("users").document(uid).get()
            .addOnSuccessListener { documentsSnapshot ->
                var id = documentsSnapshot.toObject<Id>()!!
                memberId = id.memberId!!
            }*/

        // 프로필 사진 선택
        binding.petImg.setOnClickListener {
            checkPermission {
                openGallery()
            }
        }

        // 성별 선택
        if (binding.femaleRadio.isChecked) {
            gender = "여아"
        } else {
            gender = "남아"
        }

        // 중성화 여부
        if (binding.neuterCheck.isChecked) {
            neutering = 1 // 했음
        } else {
            neutering = 2 // 안 했음
        }


        // 생년월일 선택
        binding.birthDay.setOnClickListener {
            val calender = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                birthString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                birth = "${year}-${month+1}-${dayOfMonth}"
                binding.birthDay.text = birthString
            }
            // 오늘 날짜로 지정해 놓고 다이얼로그 띄우기
            DatePickerDialog(this, dateSetListener, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 입양날짜 선택
        binding.adoptDay.setOnClickListener {
            val calender = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                adoptString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                adopt = "${year}-${month+1}-${dayOfMonth}"
                binding.adoptDay.text = adoptString
            }
            // 오늘 날짜로 지정해 놓고 다이얼로그 띄우기
            DatePickerDialog(this, dateSetListener, calender.get(Calendar.YEAR), calender.get(
                Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 스피너 설정
        val speciesAdapter = ArrayAdapter.createFromResource(this, R.array.dog_species_array, android.R.layout.simple_spinner_dropdown_item)
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectSpecies.adapter = speciesAdapter // 어댑터와 연결

        binding.selectSpecies.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                // 선택하세요 이외의 것을 클릭했을 때만 저장버튼 활성화
                binding.enrollBtn.isEnabled = !binding.selectSpecies.getItemAtPosition(position).equals("선택하세요")
                species = binding.selectSpecies.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val typeIntent = intent

        // 견묘/멤버아이디 받아오기
        var member = typeIntent.getIntExtra("memberId", 0)

        // 저장 버튼 클릭 시 반려동물 정보 저장
        binding.enrollBtn.setOnClickListener {
            name = binding.nameEdit.text.toString()

            val pet = Pet(1, name, gender, neutering, birth, adopt, species)
            retrofit.enrollPet(member, pet).enqueue(object: Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                    conimalId = response.body()!!.conimalId
                    var memberId = typeIntent.getIntExtra("memberId", 0)

                    // firebase에 memberid, conimalid 저장
                    fbAuth = FirebaseAuth.getInstance()
                    fbFirestore = FirebaseFirestore.getInstance()
                    var user = Id(memberId, conimalId, 0)

                    fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(user)
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("Post", "error, ${t}")
                }
            })

            Toast.makeText(this@EnrollActivity, "정보 저장 완료!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@EnrollActivity, NaviActivity::class.java)
            startActivity(intent)
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
                    Toast.makeText(this@EnrollActivity, "권한 거부됨", Toast.LENGTH_SHORT).show()
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

    // 갤러리에서 사진 가져오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var currentImageUrl = intent?.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                val fileName = "MEONGNYANG_" + System.currentTimeMillis() + ".png"

                try {
                    // 이미지 s3에 올리기
                    uploadImage(fileName, bitmapToFile(bitmap, fileName))
                    // 화면상에 띄우기
                    binding.petImg.setImageBitmap(bitmap)
                    updateImg(conimalId, "https://meongnyang.s3.ap-northeast-2.amazonaws.com/conimal/${fileName}")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }
    }

    private fun uploadImage(fileName: String, file: File) {
        val awsCredentials = BasicAWSCredentials(getString(R.string.access_key), getString(R.string.secret_key))
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(applicationContext).build()
        TransferNetworkLossHandler.getInstance(applicationContext)

        val uploadObserver = transferUtility.upload("meongnyang/conimal", fileName, file)

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
        var file =  File.createTempFile("image", fileName)
        var out: OutputStream? = null

        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)
        } finally {
            out?.close()
        }
        return file
    }

    private fun updateImg(conimalId: Int, img: String) {
        val retrofit = RetrofitApi.create()
        retrofit.updateProfile(conimalId, Img(img)).enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(this@EnrollActivity, "변경 실패, 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

            }
        })
    }
}