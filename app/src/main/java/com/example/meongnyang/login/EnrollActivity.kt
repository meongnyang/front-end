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
import com.example.meongnyang.DB.DBManager
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.LoginActivityEnrollBinding
import com.example.meongnyang.model.Pet
import com.example.meongnyang.model.PetModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EnrollActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityEnrollBinding
    lateinit var myHelper: myDBHelper
    lateinit var database: SQLiteDatabase
    var birthString = ""
    var adoptString = ""
    var birth = ""
    var adopt = ""
    var gender = 0
    var neutering = 0
    var species = ""
    var name = ""
    var conimalId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityEnrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeIntent = intent

        // 견묘/멤버아이디 받아오기
        var type = typeIntent.getIntExtra("type", 0)
        var memberId = typeIntent.getIntExtra("memberId", 0)

        val retrofit = RetrofitApi.create()

        // 성별 선택
        if (binding.femaleRadio.isSelected) {
            gender = 1
        } else {
            gender = 2
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
        val speciesAdapter = ArrayAdapter.createFromResource(this, R.array.species_array, android.R.layout.simple_spinner_dropdown_item)
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

        // 저장 버튼 클릭 시 반려동물 정보 저장
        binding.enrollBtn.setOnClickListener {
            name = binding.nameEdit.text.toString()

            Log.d("pet", type.toString())
            Log.d("pet", name)
            Log.d("pet", gender.toString())
            Log.d("pet", neutering.toString())
            Log.d("pet", birth)
            Log.d("pet", adopt)
            Log.d("pet", species)

            val pet = Pet(type, name, gender, neutering, birth, adopt, species)
            retrofit.enrollPet(memberId, pet).enqueue(object: Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                    Log.d("Post", response.body().toString())
                    Log.d("Post", response.body()!!.conimalId.toString())
                    conimalId = response.body()!!.conimalId
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("Post", "error, ${t}")
                }
            })
            // 반려동물 정보 저장, memberId와 conimalId 넘겨줌 메인 화면으로 넘어감
            /*myHelper = myDBHelper(this)
            database = myHelper.writableDatabase
            database.execSQL("INSERT INTO personnel VALUES (${memberId}, ${conimalId});")
            database.close()*/
            Toast.makeText(this, "정보 저장 완료!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, NaviActivity::class.java)
            intent.putExtra("memberId", memberId)
            intent.putExtra("conimalId", conimalId)
            startActivity(intent)
        }
    }
    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "personnel", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL("CREATE TABLE personnel (memberId INTEGER PRIMARY KEY, conimalId INTEGER);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS personnel")
            onCreate(db)
        }
    }
}