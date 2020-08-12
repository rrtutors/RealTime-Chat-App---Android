package com.rrtutors.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.UsersRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.rrtutors.chatapp.activities.LoginActivity

import com.rrtutors.chatapp.adapters.ContactsRecyclerAdapter
import com.rrtutors.chatapp.model.UserModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var recyclerAdapter: ContactsRecyclerAdapter
    var contactsList = arrayListOf<UserModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(menuToolbar)
       // contactsList.add(UserModel());
       // contactsList.add(UserModel());
        recyclerAdapter=ContactsRecyclerAdapter(this@MainActivity,contactsList)

            recyclerContacts.adapter=recyclerAdapter
        loadUsers()
    }

    fun loadUsers()
    {
        var usersRequest = UsersRequest.UsersRequestBuilder().setLimit(10).build()
        usersRequest.fetchNext(object:CometChat.CallbackListener<List<User>>(){
            override fun onSuccess(p0: List<User>?) {
                if(p0!=null)
                {
                    var loggedInUser = CometChat.getLoggedInUser()
                    for (user in p0)
                    {
                        // Don't add yourself (logged in user) in the list
                        if (loggedInUser.uid != user.uid)
                        {
                            contactsList.add(convertToUserModel(user))

                            // Add Online/Offline Listener
                            CometChat.addUserListener(getUniqueListenerId(user.uid), object : CometChat.UserListener() {
                                override fun onUserOffline(offlineUser: User?) {
                                    super.onUserOffline(offlineUser)
                                    user?.let {
                                        searchUserWithId(contactsList, it.uid)?.let {
                                            contactsList[it].status = "offline"
                                            recyclerAdapter?.notifyItemChanged(it)

                                        }
                                    }

                                }

                                override fun onUserOnline(user: User?) {
                                    super.onUserOnline(user)
                                    user?.let {
                                        searchUserWithId(contactsList, it.uid)?.let {
                                            contactsList[it].status = "online"
                                            recyclerAdapter?.notifyItemChanged(it)

                                        }
                                    }
                                }
                            })
                        }
                    }

                    // Update the Recycler Adapter
                    recyclerAdapter.notifyDataSetChanged()

                }
                else
                {
                    Toast.makeText(this@MainActivity, "Couldn't load the users!", Toast.LENGTH_SHORT).show()
                }

                // Hide Progress
                progressLoading.visibility = View.GONE
                recyclerContacts.visibility = View.VISIBLE
            }

            override fun onError(exception: CometChatException?) {
                progressLoading.visibility = View.GONE
                recyclerContacts.visibility = View.VISIBLE

                Toast.makeText(this@MainActivity, exception?.localizedMessage ?: "Unknown error occurred!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun convertToUserModel(user:User):UserModel
    {
        var photo=""

        return UserModel(user.uid,user.name,user.status,photo)
    }


    fun Any.getUniqueListenerId(uid: String) : String
    {
        return getCombinedID(CometChat.getLoggedInUser().uid, uid)
    }

    fun Any.getCombinedID(id1: String, id2:String) : String
    {
        var newid = ""

        var list = ArrayList<String>()
        list.add(id1)
        list.add(id2)
        list.sort()

        newid = list[0] + "-" + list[1]

        return newid
    }


    fun Any.searchUserWithId(usersList: List<UserModel>, uid: String) : Int?
    {
        var i = 0
        for (user in usersList)
        {
            if (user.uid == uid)
                return i
            i++
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            if(CometChat.getLoggedInUser()!=null)
                CometChat.logout(object : CometChat.CallbackListener<String>() {
                    override fun onSuccess(p0: String?) {
                        Toast.makeText(getApplicationContext(), "Logout ", Toast.LENGTH_SHORT).show();
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                           }

                    override fun onError(p0: CometChatException?) {
                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();  }
                })

            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}
