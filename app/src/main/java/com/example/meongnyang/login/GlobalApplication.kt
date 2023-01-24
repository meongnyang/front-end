package com.example.meongnyang.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        /*appInstance = this

        appDataBaseInstance = Room.databaseBuilder(
            appInstance.applicationContext,
            AppDataBase::class.java, "personnel"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries() // 메인스레드에서 접근 허용
            .build()*/

        KakaoSdk.init(this, "6834c4f27fbbf23e589df95359fe78c9")
    }
}