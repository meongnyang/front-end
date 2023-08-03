package com.nakyung.meongnyang.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nakyung.meongnyang.databinding.WeatherActivityMainBinding
import com.nakyung.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.util.*

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: WeatherActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var viewModelFactory: WeatherViewModelFactory
    private var addr = ""
    private var locality = ""
    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WeatherActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission {
            val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            // 위도, 경도
            val uLatitude = userNowLocation!!.latitude
            val uLongitude = userNowLocation!!.longitude

            // 내 주소 받아 오기
            addr = getAddress(userNowLocation!!)

            binding.myLocation.text = addr

            viewModelFactory = WeatherViewModelFactory(uLatitude, uLongitude, locality)
            viewModel = ViewModelProvider(this,
                viewModelFactory).get(WeatherViewModel::class.java)

            binding.apply {
                walk = viewModel
                lifecycleOwner = this@WeatherActivity
            }
        }
    }

    // 위치 권한 받기 및 표시
    @SuppressLint("MissingPermission")
    private fun requestPermission(logic: () -> Unit) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                // 권한이 허용되었을 때
                override fun onPermissionGranted() {
                    logic()
                }
                // 권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@WeatherActivity, "권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            })
            .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
            .setDeniedMessage("위치 권한을 허용해 주세요! [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }

    @SuppressLint("MissingPermission")
    private fun getAddress(location: Location): String {
        var nowAddr = "현재 위치를 사용할 수 없습니다."
        val geocoder = Geocoder(this, Locale.KOREA)
        Log.d("why", geocoder.toString())
        try {
            if (geocoder != null) {
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (address != null && address.size > 0) {
                    nowAddr = address.first().getAddressLine(0)
                    var token = nowAddr.split(" ")
                    nowAddr = token[1] + " " + token[2]
                    locality = token[2]
                } else {
                    nowAddr = "서울특별시 노원구"
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, "주소를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        return nowAddr
    }
}