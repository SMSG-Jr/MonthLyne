package com.sergio.monthlyne.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.makeramen.roundedimageview.RoundedImageView
import com.sergio.monthlyne.R
import com.sergio.monthlyne.entity.PostInformation
import com.sergio.monthlyne.entity.UserInformation

class ProfileActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private lateinit var bottomNavigation : BottomNavigationView

    private lateinit var userEmail : AppCompatTextView
    private lateinit var userName : AppCompatTextView
    private lateinit var userPhoto : RoundedImageView
    private lateinit var postProfilePhoto : RoundedImageView
    private lateinit var postProfileName : AppCompatTextView
    private lateinit var postProfileDate : AppCompatTextView
    private lateinit var postProfileMessage : AppCompatTextView
    private lateinit var postProfileDislike : AppCompatTextView
    private lateinit var postProfileLike : AppCompatTextView
    private lateinit var postProfileImageLike : AppCompatImageView
    private lateinit var postProfileImageDislike : AppCompatImageView
    private lateinit var noPostText : AppCompatTextView
    private lateinit var buttonAddPost : FloatingActionButton

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewsById()
        postInvisible()

        setSupportActionBar(toolbar)
        bottomNavigation.setOnNavigationItemSelectedListener { itemSelected ->
            when(itemSelected.itemId){
                R.id.bottom_nav_post -> configPostButton()
                R.id.bottom_nav_post_ranking ->configTimelineButton()
            }
            true
        }
        buttonAddPost.setOnClickListener {
            addPostButtonConfig()
        }
    }

    override fun onResume() {
        super.onResume()
        checkUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout_button-> configLogoutButton()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun configLogoutButton() {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun configTimelineButton() {
        startActivity(Intent(this, TimeLineActivity::class.java))
        finish()
    }
    private fun configPostButton() {
        startActivity(Intent(this, FeedActivity::class.java))
        finish()
    }

    private fun findViewsById() {
        userEmail = findViewById(R.id.user_email)
        userName = findViewById(R.id.user_name)
        userPhoto = findViewById(R.id.profile_user_photo)
        postProfilePhoto = findViewById(R.id.post_profile_user_photo)
        postProfileName = findViewById(R.id.post_profile_user_name)
        postProfileDate = findViewById(R.id.post_profile_date)
        postProfileMessage = findViewById(R.id.post_profile_message)
        postProfileLike = findViewById(R.id.post_profile_like)
        postProfileDislike = findViewById(R.id.post_profile_dislike)
        postProfileImageLike = findViewById(R.id.post_image_like)
        postProfileImageDislike = findViewById(R.id.post_image_dislike)
        noPostText = findViewById(R.id.text_no_post)
        buttonAddPost = findViewById(R.id.FAB_add_post)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigation = findViewById(R.id.bottom_navigation)

    }

    private fun addPostButtonConfig() {
        startActivity(Intent(this, AddPostActivity::class.java))
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            updateProfileInfo(firebaseUser)
            updatePostProfileInfo()
        }
    }

    private fun updateProfileInfo(firebaseUser: FirebaseUser) {
        val uEmail = firebaseUser.email
        val uName = firebaseUser.displayName
        val uPhoto = firebaseUser.photoUrl
        userEmail.text = uEmail
        userName.text = uName
        Glide.with(this).load(uPhoto).into(userPhoto)
    }

    private fun updatePostProfileInfo() {
        val userID = firebaseAuth.uid.toString()
        db.collection("Users").document(userID).get()
                .addOnSuccessListener { result ->
                    val user: UserInformation = result.toObject()!!
                    val postId = user.postId
                    if (postId != "") {
                        db.collection("Posts").document(postId).get()
                                .addOnSuccessListener { postDoc ->
                                    val postInfo: PostInformation = postDoc.toObject()!!
                                    postProfileName.text = postInfo.postName
                                    postProfileDate.text = postInfo.postDate
                                    postProfileMessage.text = postInfo.postContent
                                    postProfileLike.text = postInfo.postLikeCounter.size.toString()
                                    postProfileDislike.text = postInfo.postDislikeCounter.size.toString()
                                    Glide.with(this).load(postInfo.postPhotoURL).into(postProfilePhoto)
                                    postVisible()
                                }.addOnFailureListener { exception ->
                                    Log.w("DataBase", "failed to get post document: $exception")
                                }
                    }
                }.addOnFailureListener { exception ->
                    Log.w("DataBase", "failed to get postID: $exception")
                }
    }

    private fun postVisible() {
        postProfilePhoto.visibility = View.VISIBLE
        postProfileName.visibility = View.VISIBLE
        postProfileDate.visibility = View.VISIBLE
        postProfileMessage.visibility = View.VISIBLE
        postProfileImageLike.visibility = View.VISIBLE
        postProfileLike.visibility = View.VISIBLE
        postProfileImageDislike.visibility = View.VISIBLE
        postProfileDislike.visibility = View.VISIBLE
        noPostText.visibility = View.INVISIBLE
    }

    private fun postInvisible() {
        postProfilePhoto.visibility = View.INVISIBLE
        postProfileName.visibility = View.INVISIBLE
        postProfileDate.visibility = View.INVISIBLE
        postProfileMessage.visibility = View.INVISIBLE
        postProfileImageLike.visibility = View.INVISIBLE
        postProfileLike.visibility = View.INVISIBLE
        postProfileImageDislike.visibility = View.INVISIBLE
        postProfileDislike.visibility = View.INVISIBLE
        noPostText.visibility = View.VISIBLE
    }
}