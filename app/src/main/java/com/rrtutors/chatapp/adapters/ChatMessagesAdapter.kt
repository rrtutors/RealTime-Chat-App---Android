package com.rrtutors.chatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.rrtutors.chatapp.R
import com.rrtutors.chatapp.model.MessageModel
import com.rrtutors.chatapp.model.UserModel
import kotlinx.android.synthetic.main.chat_message_item_layout.view.*

class ChatMessagesAdapter(val context: Context, val messageList:ArrayList<MessageModel>): RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewholder {

        return ChatMessageViewholder(LayoutInflater.from(parent.context).inflate(R.layout.chat_message_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ChatMessageViewholder, position: Int) {
        var messageModel=messageList[position]

        if (messageModel.isMine)
        {
            holder.cardMyMessage.visibility = View.VISIBLE
            holder. cardOtherMessage.visibility = View.GONE
            holder. txtMyMessage.text = messageModel.message
        }
        else
        {
            holder. cardMyMessage.visibility = View.GONE
            holder. cardOtherMessage.visibility = View.VISIBLE
            holder. txtOtherMessage.text = messageModel.message
        }
    }

    inner class ChatMessageViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtMyMessage = itemView.findViewById<TextView>(R.id.txtMyMessage)
        var txtOtherMessage = itemView.findViewById<TextView>(R.id.txtOtherMessage)
        var cardMyMessage = itemView.findViewById<MaterialCardView>(R.id.cardChatMyMessage)
        var  cardOtherMessage = itemView.findViewById<MaterialCardView>(R.id.cardChatOtherMessage)
    }
}