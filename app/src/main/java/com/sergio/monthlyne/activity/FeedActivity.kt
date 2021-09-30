package com.sergio.monthlyne.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.adapter.PostAdapter
import com.sergio.monthlyne.entity.PostInformation

class FeedActivity : AppCompatActivity() {

    private lateinit var postRecyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private var postList = emptyList<PostInformation>()

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val userUID = firebaseAuth.uid

    private val postAdapter : PostAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        findViewsById()
        setSupportActionBar(toolbar)
        getPostData()

        postRecyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.profile_button-> configProfileButton()
            R.id.logout_button-> configLogoutButton()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configProfileButton() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun configLogoutButton() {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (userUID != null) {
            db.collection("Posts").get()
                    .addOnSuccessListener { result ->
                        postList = result.toObjects(PostInformation::class.java)
                        postAdapter.update(postList)
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Post loading", "error getting document:",exception)
                    }
        }
    }

    private fun findViewsById() {
        postRecyclerView = findViewById(R.id.user_post_list)
        toolbar = findViewById(R.id.toolbar)
    }

    private fun getPostData() {

    }
}