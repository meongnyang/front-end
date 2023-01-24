package com.example.meongnyang.home

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.DB.DBManager
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.Pet
import com.example.meongnyang.model.PetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    /*val name : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val strType : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val count : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun userID() {
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    val jsonStr = "{\"conimalId\":1, \"type\": 1, \"name\":\"만두\", \"gender\": \"여아\", \"neutering\": 1, \"birth\": \"2019-03-16\", \"adopt\":\"2019-06-14\", \"species\": \"비숑프리제\", \"img\": \"img.png\"}"

    val petInfo = JSONObject(jsonStr)


    val conimalId = petInfo.getInt("conimalId")
    val type = petInfo.getInt("type")
    val petName = petInfo.getString("name")
    val gender = petInfo.getString("gender")
    val neutering = petInfo.getInt("neutering")
    val birth = petInfo.getString("birth")
    val adopt = petInfo.getString("adopt")
    val species = petInfo.getString("species")
    val img = petInfo.getString("img")

    private val pet = Pet(type, petName, gender, neutering, birth, adopt, species, img)

    init {
        viewModelScope.launch {
            name.value = pet.name
            strType.value = strType(pet.type)
            count.value = dayCount(pet.adopt)
        }
    }*/

    // 함수는 다른 곳에 만들어 주기
    // 숫자를 견, 묘로 바꿔주는 함수
    /*private fun strType(type: Int): String {
        var strType = ""
        strType = if (type == 1) {
            "견"
        } else "묘"

        return strType
    }

    // 입양 후 날짜 카운트 함수
    private fun dayCount(adopt: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val adoptDate = formatter.parse(adopt)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val diff = today - adoptDate.time
        val count = (diff / (24*60*60*1000) + 1).toString()

        return count
    }*/
}
