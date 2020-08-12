package com.rrtutors.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cometchat.pro.core.CometChat
import com.rrtutors.chatapp.MainActivity
import com.rrtutors.chatapp.R

class SpashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)

        Handler().postDelayed(Runnable {
            if(CometChat.getLoggedInUser()!=null)
            {
                startActivity(Intent(this@SpashActivity,MainActivity::class.java))
                finish()
            }else
            {
                startActivity(Intent(this@SpashActivity,LoginActivity::class.java))
                finish()
            }
        },2000)
    }
}
