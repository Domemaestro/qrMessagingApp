package com.example.qrapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qrapp.DataFile.Contacts
import com.example.qrapp.MessageActivity
import com.example.qrapp.R

class Contact_RecyclerView(val context: Context?, val contactList: ArrayList<Contacts>):RecyclerView.Adapter<Contact_RecyclerView.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflates = LayoutInflater.from(parent.context).inflate(R.layout.chat_card,parent,false)
        return MyViewHolder(inflates)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = contactList[position]

        Glide.with(context!!).load(contactList[position].userImg).into(holder.personProfImg)
        holder.personName.text = currentUser.name
        holder.itemView.setOnClickListener {
            val toMessageActivity = Intent(context,MessageActivity::class.java)
            toMessageActivity.putExtra("Name",currentUser.name)
            toMessageActivity.putExtra("uid", currentUser.uid)
            toMessageActivity.putExtra("image",currentUser.userImg)
            context.startActivity(toMessageActivity)
        }

    }

    override fun getItemCount(): Int {
        return contactList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val personName = itemView.findViewById<TextView>(R.id.person_id_tv)
        val personProfImg = itemView.findViewById<ImageView>(R.id.person_prof_pic_img)
    }
}