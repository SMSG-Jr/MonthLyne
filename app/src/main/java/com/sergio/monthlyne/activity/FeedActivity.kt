package com.sergio.monthlyne.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.adapter.PostAdapter
import com.sergio.monthlyne.entity.PostInformation

class FeedActivity : AppCompatActivity() {
    // TODO: 04/10/2021 add a bottom navigation to access profile and timeline activity
    private lateinit var postRecyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private var postList = emptyList<PostInformation>()

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val postAdapter : PostAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        findViewsById()
        setSupportActionBar(toolbar)
// TODO: 04/10/2021 add like/dislike button functionality
        postRecyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        checkUser()
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
            R.id.timeline_button-> configTimelineButton()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configProfileButton() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
    
    private fun configTimelineButton() {
        startActivity(Intent(this, TimeLineActivity::class.java))
    }

    private fun configLogoutButton() {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            setPostsData()
        }
    }

    private fun setPostsData() {
        db.collection("Posts").get()
                .addOnSuccessListener { result ->
                    postList = result.toObjects(PostInformation::class.java)
                    postAdapter.update(postList)
                }
                .addOnFailureListener { exception ->
                    Log.w("DataBase", "error getting document:", exception)
                }
    }

    private fun findViewsById() {
        postRecyclerView = findViewById(R.id.recyclerview_user_post)
        toolbar = findViewById(R.id.toolbar)
    }

}