package com.sergio.monthlyne.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.entity.PostInformation
import com.sergio.monthlyne.entity.UserInformation

class AddPostActivity : AppCompatActivity() {

    private lateinit var editPostMessage : AppCompatEditText
    private lateinit var sendPostButton : FloatingActionButton

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        findViewsById()
        sendPostButton.setOnClickListener {
            sendPostButtonConfig()
        }
    }

    private fun sendPostButtonConfig() {
        val postMessage = editPostMessage.text.toString()
        if (postMessage != ""){
            // TODO: 04/10/2021 add confirmation box
            addPostToDatabase(postMessage)
            finish()
        }else{
            editPostMessage.error = "Post cannot be empty."
        }
    }

    private fun addPostToDatabase(postMessage: String) {
        val firebaseUser = firebaseAuth.currentUser
        val uid = firebaseUser?.uid.toString()

        db.collection("Users").document(uid).get()
            .addOnSuccessListener { userDocument->
                val user : UserInformation? = userDocument.toObject()
                val uPhotoURL = user?.userPhotoURL.toString()
                val uName = user?.userName.toString()
                val uPostId = user?.postId.toString()
                // TODO: 04/10/2021 add current date to post
                val newPost = PostInformation(uPostId, uid,"", postMessage, "0", uName, uPhotoURL, mutableListOf(), mutableListOf())
                if (uPostId == ""){
                    db.collection("Posts").add(newPost)
                            .addOnSuccessListener { documentReference ->
                                Log.w("DataBase", "updated post ID in user: ${documentReference.id}")
                                db.collection("Users").document(uid).update("postId", documentReference.id)
                            }.addOnFailureListener { e->
                                Log.w("DataBase", "failed to update post ID in user: $e")
                            }
                }else{
                    db.collection("Posts").document(uPostId).set(newPost)
                            .addOnSuccessListener { Log.w("DataBase", "Replaced old post with new one.") }
                            .addOnFailureListener{ e->
                                Log.w("DataBase", "Failed to update user post: $e")
                            }
                }
        }.addOnFailureListener { exception->
                Log.w("DataBase", "failed to get user document: $exception", )
            }



    }


    private fun findViewsById() {
        editPostMessage = findViewById(R.id.edit_post_message)
        sendPostButton = findViewById(R.id.FAB_send_post)
    }
}