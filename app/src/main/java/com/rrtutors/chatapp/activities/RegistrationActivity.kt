package com.rrtutors.chatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.core.CometChat
import android.util.Log
import com.rrtutors.chatapp.MainActivity
import com.rrtutors.chatapp.R
import com.rrtutors.chatapp.utils.AppConfig


class RegistrationActivity : AppCompatActivity() {

    var uName:String?=null;
    var uId:String?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.rrtutors.chatapp.R.layout.activity_registration)
        input_uid!!.isEndIconVisible = false
        edit_uid.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
               if(p0!!.length>0)
               {
                   input_uid!!.isEndIconVisible = true
               }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        edit_uid.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (edit_uid.text!!.toString().isEmpty()||edit_name.text!!.toString().isEmpty()) {
                    Toast.makeText(this@RegistrationActivity, "Fill all details", Toast.LENGTH_LONG).show()
                    input_uid!!.isEndIconVisible = false
                } else {
                    loginProgress.visibility = View.VISIBLE
                    input_uid!!.isEndIconVisible = false
                    signup()
                }
            }
             true;
        }
        input_uid.setEndIconOnClickListener {
            uName=edit_name.text.toString().trim();
            uId=edit_uid.text.toString().trim();
            if(uName!!.isEmpty())
            {
                Toast.makeText(applicationContext,"Please enter Name",Toast.LENGTH_SHORT).show()
                return@setEndIconOnClickListener;
            }
            if(uId!!.isEmpty())
            {
                Toast.makeText(applicationContext,"Please enter User Id",Toast.LENGTH_SHORT).show()
                return@setEndIconOnClickListener;
            }
            loginProgress.visibility = View.VISIBLE
            input_uid!!.isEndIconVisible = false
            signup()

        }

        edit_uid.setOnEditorActionListener { textView, i, keyEvent ->

            if(i==EditorInfo.IME_ACTION_DONE)
            {
                uName=edit_name.text.toString().trim();
                uId=edit_uid.text.toString().trim();
                if(uName!!.isEmpty())
                {
                    Toast.makeText(applicationContext,"Please enter Name",Toast.LENGTH_SHORT).show()
                  true;
                }
                if(uId!!.isEmpty())
                {
                    Toast.makeText(applicationContext,"Please enter User Id",Toast.LENGTH_SHORT).show()
                     true;
                }

                loginProgress.visibility = View.VISIBLE
                    input_uid!!.isEndIconVisible = false
                    signup()

            }
            true;
        }

    }
    fun signup()
    {
        val authKey = AppConfig.AppDetails.API_KEY // Replace with your App Auth Key
        val user = User()
        user.uid = uId// Replace with the UID for the user to be created
        user.name = uName
        CometChat.createUser(user, authKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d("createUser", user.toString());
                startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                finish()
            }

            override fun onError(e: CometChatException) {
                Log.e("createUser", e.message!!)
                input_uid!!.isEndIconVisible = true
               loginProgress.visibility = View.GONE
                Toast.makeText(this@RegistrationActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        })
        txt_signup.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {

        startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))
        finish()
    }
}
