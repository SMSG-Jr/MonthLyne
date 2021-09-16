package com.sergio.monthlyne.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.adapter.PostAdapter

class FeedActivity : AppCompatActivity() {

    private lateinit var postRecyclerView : RecyclerView

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val userUID = firebaseAuth.uid

    private val postAdapter : PostAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        findViewsById()
        getPostData()

        postRecyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        if (userUID != null) {
            db.collection("Users").document(userUID).get()//----------------------------------------------
        }
    }

    private fun findViewsById() {
        postRecyclerView = findViewById(R.id.user_post_list)
    }

    private fun getPostData() {

    }
}