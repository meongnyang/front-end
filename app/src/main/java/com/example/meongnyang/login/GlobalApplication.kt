package com.example.meongnyang.login

import android.app.Application
import com.example.meongnyang.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "6834c4f27fbbf23e589df95359fe78c9")
    }
}