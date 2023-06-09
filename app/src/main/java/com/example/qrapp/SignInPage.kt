package com.example.qrapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInPage : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var  email_error_tv:TextView
    private lateinit var  pass_error_tv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        //firebase Authentication
        mAuth = FirebaseAuth.getInstance()

        //animation references
        val left_anim = AnimationUtils.loadAnimation(this,R.anim.left_anim)
        val bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim)

        val home_btn = findViewById<Button>(R.id.back_btn)
        val intro_tv = findViewById<TextView>(R.id.intro_tv)
        val intro2_tv = findViewById<TextView>(R.id.intro2_tv)
        val container_img = findViewById<ImageView>(R.id.signIn_box_img)
        val et_email = findViewById<EditText>(R.id.email_et)
        val et_pass = findViewById<EditText>(R.id.pass_et)
        val log_btn = findViewById<Button>(R.id.login_btn)

        email_error_tv = findViewById(R.id.email_error_tv)
        pass_error_tv = findViewById(R.id.password_error_tv)

        home_btn.startAnimation(left_anim)
        intro_tv.startAnimation(left_anim)
        intro2_tv.startAnimation(left_anim)

        container_img.startAnimation(bottom_anim)
        et_email.startAnimation(bottom_anim)
        et_pass.startAnimation(bottom_anim)
        log_btn.startAnimation(bottom_anim)

        val backToStartPage = findViewById<Button>(R.id.back_btn)


        val toStartPage = Intent(this,MainActivity::class.java)


        backToStartPage.setOnClickListener {
            startActivity(toStartPage)
            finish()
        }

        log_btn.setOnClickListener {
            val email = et_email.text.toString()
            val pass = et_pass.text.toString()

            login(email, pass)
        }
    }

    private fun login(email:String,pass:String){
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val toMainPage = Intent(this,MainChatPage::class.java)
                    startActivity(toMainPage)
                    finish()

                } else {
                    email_error_tv.setText("Invalid Email")
                    pass_error_tv.setText("Invalid Password")
                }
            }

    }
}