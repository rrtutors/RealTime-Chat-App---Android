package com.rrtutors.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.MessagesRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.BaseMessage
import com.cometchat.pro.models.TextMessage
import com.cometchat.pro.models.User
import com.rrtutors.chatapp.R
import com.rrtutors.chatapp.adapters.ChatMessagesAdapter
import com.rrtutors.chatapp.model.MessageModel
import com.rrtutors.chatapp.model.UserModel
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    var user: UserModel? = null
    val messagesList = arrayListOf<MessageModel>()
    var messagesAdapter: ChatMessagesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        if (intent.extras != null)
        {
            user = UserModel (
                uid = intent.getStringExtra("uid")!!,
                name = intent.getStringExtra("name")!!,
                status = intent.getStringExtra("status")!!,
                photoUrl = intent.getStringExtra("photo")!!

            )
            menuToolbar?.setTitle(""+ user!!.name)
            messagesAdapter=ChatMessagesAdapter(this@ChatActivity,messagesList)
            recyclerMessages.adapter=messagesAdapter;
            loadChatMessages();
            loadConversationMessages()
            menuToolbar.setNavigationOnClickListener {
                finish()
            }
        }
        else
        {
            finish()
            return
        }


        // Message Box
        txtMessageBox.setOnEditorActionListener { v, actionId, event ->
            when(actionId)
            {
                EditorInfo.IME_ACTION_GO -> {
                    sendMessage(txtMessageBox.text.toString())
                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener false
        }

        // Send Button
        imgSend.setOnClickListener {
            sendMessage(txtMessageBox.text.toString())
        }

    }

    private fun setUserStatus(isOnline: Boolean)
    {
        if (isOnline)
        {
            supportActionBar?.subtitle = Html.fromHtml("<font color='#149214'>online</font>")
        }
        else
        {
            supportActionBar?.subtitle = Html.fromHtml("<font color='#575757'>offline</font>")
        }
    }

    private fun sendMessage(message: String) {
        if (!message.isEmpty())
        {
            user?.let {
                val receiverID: String = it.uid
                val messageText = message
                val receiverType = CometChatConstants.RECEIVER_TYPE_USER

                val textMessage = TextMessage(receiverID, messageText,receiverType)

                CometChat.sendMessage(textMessage, object : CometChat.CallbackListener<TextMessage>() {
                    override fun onSuccess(p0: TextMessage?) {

                    }

                    override fun onError(p0: CometChatException?) {

                    }
                })

                var messageModel = MessageModel(message, true)
                messagesList.add(messageModel)
                messagesAdapter?.notifyItemInserted(messagesList.size-1)
                recyclerMessages.scrollToPosition(messagesList.size-1)

                // Clear the message box
                txtMessageBox.setText("")
            }
        }
    }


    fun loadChatMessages()
    {
        CometChat.addMessageListener(getUniqueListenerId(user!!.uid), object : CometChat.MessageListener() {
            override fun onTextMessageReceived(message: TextMessage?) {
                message?.let {
                    messagesList.add(MessageModel(message = it.text, isMine = false))
                    messagesAdapter?.notifyItemInserted(messagesList.size)
                    recyclerMessages.scrollToPosition(messagesList.size-1)
                }
            }
        })

        // Add Online/Offline Listener
        CometChat.addUserListener(getUniqueListenerId(user!!.uid), object : CometChat.UserListener() {
            override fun onUserOffline(offlineUser: User?) {
                super.onUserOffline(offlineUser)
                setUserStatus(false)
            }

            override fun onUserOnline(user: User?) {
                super.onUserOnline(user)
                setUserStatus(true)
            }
        })
    }
    fun loadConversationMessages()
    {
        user?.let {

            // Show Progress Bar
            progressLoading.visibility = View.VISIBLE
            recyclerMessages.visibility = View.GONE

            var messagesRequest = MessagesRequest.MessagesRequestBuilder()
                .setUID(it.uid)
                .build()

            messagesRequest.fetchPrevious(object : CometChat.CallbackListener<List<BaseMessage>>() {
                override fun onSuccess(msgList: List<BaseMessage>?) {
                    // Hide Progress bar
                    progressLoading.visibility = View.GONE
                    recyclerMessages.visibility = View.VISIBLE

                    if (msgList != null)
                    {
                        for (msg in msgList)
                        {
                            if (msg is TextMessage)
                            {
                                messagesList.add(msg.convertToMessageModel())
                            }
                        }

                        // Update RecyclerView
                        messagesAdapter?.notifyDataSetChanged()
                    }
                    else
                    {
                        Toast.makeText(this@ChatActivity, "Couldn't fetch messages!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(exception: CometChatException?) {
                    // Hide Progress bar
                    progressLoading.visibility = View.GONE
                    recyclerMessages.visibility = View.VISIBLE

                    Toast.makeText(this@ChatActivity, exception?.localizedMessage ?: "Unknown error occurred!", Toast.LENGTH_SHORT).show()
                }
            })
        }
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
    fun TextMessage.convertToMessageModel() : MessageModel
    {
        return MessageModel(
            message = text,
            isMine = sender.uid == CometChat.getLoggedInUser().uid
        )
    }


}
