package com.example.meongnyang.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meongnyang.R
import com.example.meongnyang.databinding.MapActivityPlayBinding
import com.example.meongnyang.model.MapList
import com.example.meongnyang.model.Place
import com.example.meongnyang.model.ResultSearchKeyword
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PlayMapActivity : AppCompatActivity() {
    private lateinit var playList: ArrayList<Place>

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 048598205d508fada0864a9cff58740f" // REST API 키
    }

    private lateinit var binding: MapActivityPlayBinding
    private val listItems = arrayListOf<MapList>() // 리사이클러뷰 아이템
    private val mapListAdapter = MapListAdapter(listItems)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapActivityPlayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        playList = arrayListOf()

        // 권한
        requestPermission()

        val mapView = MapView(this@PlayMapActivity)
        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        show(mapView)

        // 리사이클러뷰
        binding.playListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.playListView.adapter = mapListAdapter

        // 리스트 아이템 클릭 시 해당 위치로 이동
        mapListAdapter.setItemClickListener(object: MapListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(listItems[position].y, listItems[position].x)
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)
                mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            }
        })
    }

    // 위치 권한 받기 및 표시
    @SuppressLint("MissingPermission")
    private fun requestPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                // 권한이 허용되었을 때
                override fun onPermissionGranted() {

                }
                // 권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@PlayMapActivity, "권한 거부됨", Toast.LENGTH_SHORT).show()
                }
            })
            .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
            .setDeniedMessage("위치 권한을 허용해 주세요! [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }

    @SuppressLint("MissingPermission")
    private fun show(mapView: MapView) {
        nowLocation(mapView)

        // 동물병원 보여주기
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // 위도, 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude

        mapView.setZoomLevel(7, true)
        requestSearchLocalDetail(mapView, "반려견놀이터", uLongitude!!.toDouble(), uLatitude!!.toDouble(), 10)
    }

    // 사용자 현재 위치에 점 표시하기
    @SuppressLint("MissingPermission")
    private fun nowLocation(mapView: MapView) {
        // 현재 사용자 위치추적
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // 위도, 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

        // 현 위치에 마커 찍기
        val marker = MapPOIItem()
        marker.mapPoint = uNowPosition
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)
    }

    // 반경 5km 이내 위치 찾기
    @SuppressLint("MissingPermission")
    private fun requestSearchLocalDetail(mapView: MapView, keyword: String, X: Double, Y: Double, size: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MapActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)
        val call = api.getSearchLocationDetail(MapActivity.API_KEY, keyword, X.toString(), Y.toString(), size) // 검색 조건 입력

        // API 서버에 요청하기
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨 있음)
                playList.clear()
                mapView.removeAllPOIItems()
                playList.addAll(response.body()!!.documents)

                listItems.clear()
                for (document in response.body()!!.documents) {
                    val item = MapList(document.place_name,
                        document.road_address_name,
                        document.phone,
                        document.x.toDouble(),
                        document.y.toDouble())
                    listItems.add(item)
                }
                mapListAdapter.notifyDataSetChanged()

                var tagNum = 20
                for (place: Place in playList) {
                    val marker = MapPOIItem()
                    marker.itemName = place.place_name
                    marker.tag = tagNum++
                    val x = place.y.toDouble()
                    val y = place.x.toDouble()

                    val mapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(x, y)
                    marker.mapPoint = mapPoint
                    marker.markerType = MapPOIItem.MarkerType.RedPin
                    marker.isCustomImageAutoscale = false
                    mapView.addPOIItem(marker)
                }

            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }
        })
    }
}