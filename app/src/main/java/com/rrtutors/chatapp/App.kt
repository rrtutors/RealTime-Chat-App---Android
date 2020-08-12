package com.rrtutors.chatapp

import android.app.Application
import android.util.Log
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.rrtutors.chatapp.utils.AppConfig

class App :Application() {
    val appID:String=AppConfig.AppDetails.API_ID // Replace with your App ID
    val region:String="us" // Replace with your App Region ("eu" or "us")

    val TAG:String="App "
    val appSettings = AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build()


    override fun onCreate() {
        super.onCreate()
        CometChat.init(this,appID,appSettings, object : CometChat.CallbackListener<String>() {
            override fun onSuccess(p0: String?) {
                Log.d(TAG, "Initialization completed successfully")
            }

            override fun onError(p0: CometChatException?) {
                Log.d(TAG, "Initialization failed with exception: " + p0?.message)
            }

        })
    }



}