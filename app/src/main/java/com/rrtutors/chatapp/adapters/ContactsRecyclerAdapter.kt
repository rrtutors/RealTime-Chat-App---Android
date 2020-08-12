package com.rrtutors.chatapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.rrtutors.chatapp.R
import com.rrtutors.chatapp.activities.ChatActivity
import com.rrtutors.chatapp.model.UserModel

class ContactsRecyclerAdapter(val context:Context,val contactList:ArrayList<UserModel>): RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        Log.v("Count","Count "+contactList.size)
        return contactList.size;
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        setAvatarImage(""+contactList.get(position).name.get(0),holder.imgContactPhoto)
        holder.txtUsername.text=contactList[position].name
        holder.txtStatus.text=contactList[position].status
        if(contactList[position].status.equals("offline"))
            holder.txtStatus.setTextColor(Color.parseColor("#D2D2D2"))
        else
            holder.txtStatus.setTextColor(Color.parseColor("#1ba261"))
        holder.cardRootLayout.setOnClickListener {

           var intent= Intent(context,ChatActivity::class.java)
            intent.putExtra("uid",contactList[position].uid)
            intent.putExtra("name",contactList[position].name)
            intent.putExtra("status",contactList[position].status)
            intent.putExtra("photo",contactList[position].photoUrl)

            context.startActivity(intent)
        }
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgContactPhoto=itemView.findViewById<ImageView>(R.id.imgContactPhoto)
        var txtUsername=itemView.findViewById<TextView>(R.id.txtUsername)
        var txtStatus=itemView.findViewById<TextView>(R.id.txtStatus)
        var cardRootLayout=itemView.findViewById<View>(R.id.cardRootLayout)

    }
    fun setAvatarImage(letter: String,imgContactPhoto:ImageView)
    {
        var generator = ColorGenerator.MATERIAL
        var color = generator.randomColor

        var drawable = TextDrawable.builder().buildRound(letter, color)
        imgContactPhoto.setImageDrawable(drawable)
    }
}