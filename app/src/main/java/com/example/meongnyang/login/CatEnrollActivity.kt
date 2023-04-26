package com.example.meongnyang.login

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.LoginActivityEnrollBinding
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.Pet
import com.example.meongnyang.model.PetModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CatEnrollActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityEnrollBinding
    private var fbAuth: FirebaseAuth? = null
    var fbFirestore: FirebaseFirestore? = null
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

        // 성별 선택
        if (binding.femaleRadio.isSelected) {
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
        val speciesAdapter = ArrayAdapter.createFromResource(this, R.array.cat_species_array, android.R.layout.simple_spinner_dropdown_item)
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
        var type = typeIntent.getIntExtra("type", 0)
        var member = typeIntent.getIntExtra("memberId", 0)

        // 저장 버튼 클릭 시 반려동물 정보 저장
        binding.enrollBtn.setOnClickListener {
            name = binding.nameEdit.text.toString()

            val pet = Pet(2, name, gender, neutering, birth, adopt, species, 1)
            retrofit.enrollPet(member, pet).enqueue(object: Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                    var conimalId = response.body()!!.conimalId
                    var memberId = typeIntent.getIntExtra("memberId", 0)

                    // firebase에 memberid, conimalid 저장
                    fbAuth = FirebaseAuth.getInstance()
                    fbFirestore = FirebaseFirestore.getInstance()
                    var user = Id(memberId, conimalId)

                    fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(user)
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("Post", "error, ${t}")
                }
            })

            Toast.makeText(this@CatEnrollActivity, "정보 저장 완료!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@CatEnrollActivity, NaviActivity::class.java)
            startActivity(intent)
        }
    }
}