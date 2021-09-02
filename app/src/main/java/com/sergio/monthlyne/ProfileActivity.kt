package com.sergio.monthlyne

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var userEmail : AppCompatTextView
    private lateinit var userName : AppCompatTextView
    private lateinit var logoutBtn : AppCompatButton
    private lateinit var userPhoto : RoundedImageView


    private lateinit var firebaseAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        findViewsById()
        checkUser()

        logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun findViewsById() {
        userEmail = findViewById(R.id.user_email)
        userName = findViewById(R.id.user_name)
        logoutBtn = findViewById(R.id.logout_button)
        userPhoto = findViewById(R.id.user_photo)
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            val uEmail = firebaseUser.email
            val uName = firebaseUser.displayName
            val uPhoto = firebaseUser.photoUrl
            userEmail.text = uEmail
            userName.text = uName
            Glide.with(this).load(uPhoto).into(userPhoto)
        }
    }

}