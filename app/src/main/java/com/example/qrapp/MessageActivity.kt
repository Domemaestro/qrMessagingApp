package com.example.qrapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qrapp.Adapter.MessageAdapter
import com.example.qrapp.DataFile.MessageFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MessageActivity : AppCompatActivity() {

    private lateinit var back_btn:Button
    private lateinit var send_btn:Button
    private lateinit var messageBox:EditText
    private lateinit var chatPersonName:TextView
    private lateinit var chatterProf:ImageView

    private lateinit var chatRecyclerView:RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<MessageFile>

    private lateinit var dbRef:DatabaseReference
    private lateinit var mAuth:FirebaseAuth

    var senderRoom : String? = null
    var receiverRoom :String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val name = intent.getStringExtra("Name") //from hori_rcV
        val receiverUid = intent.getStringExtra("uid") //from hori_rcV
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        dbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()

        back_btn = findViewById(R.id.back_btn)
        chatPersonName = findViewById(R.id.uName_tv)
        send_btn = findViewById(R.id.send_btn)
        messageBox = findViewById(R.id.message_box_et)

        chatterProf = findViewById(R.id.userProf_img)
        val chatterProf2 = findViewById<ImageView>(R.id.userProf_img2)
        chatRecyclerView = findViewById(R.id.chat_rc_view)
        chatPersonName.text = name
        Glide.with(this).load(intent.getStringExtra("image")).into(chatterProf)
        Glide.with(this).load(intent.getStringExtra("ImgUri")).into(chatterProf2)

        messageList = ArrayList()

        messageAdapter = MessageAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter


        back_btn.setOnClickListener {
            val backToMainPage = Intent(this,MainChatPage::class.java)
            startActivity(backToMainPage)
        }

        dbRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(MessageFile::class.java)
                        messageList.add(message!!)
                    }
                    chatRecyclerView.smoothScrollToPosition(chatRecyclerView.bottom)

                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        send_btn.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = MessageFile(message,senderUid)

            dbRef.child("Chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child("Chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
}