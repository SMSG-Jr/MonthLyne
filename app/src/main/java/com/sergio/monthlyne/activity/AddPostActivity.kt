package com.sergio.monthlyne.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.entity.PostInformation
import com.sergio.monthlyne.entity.UserInformation
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {

    private lateinit var editPostMessage : AppCompatEditText
    private lateinit var sendPostButton : FloatingActionButton

    private val currentDate = Calendar.getInstance()
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
            addPostConfirmDialog(postMessage)

        }else{
            editPostMessage.error = "Post cannot be empty."
        }
    }

    private fun addPostConfirmDialog(postMessage: String) {

            val confirmAlertDialog = AlertDialog.Builder(this)
            confirmAlertDialog.setTitle("Send Post?")
            confirmAlertDialog.setMessage("Your previous post will be replaced. \nDo you wish to continue.")

            confirmAlertDialog.setPositiveButton("Post!"){ _: DialogInterface, _:Int->
                addPostToDatabase(postMessage)
                Toast.makeText(this, "Post Sent!", Toast.LENGTH_SHORT).show()
                finish()
            }
            confirmAlertDialog.setNegativeButton("Cancel."){ dialog: DialogInterface, _:Int ->
                dialog.dismiss()
            }
            confirmAlertDialog.show()
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
                val newPost = PostInformation(uPostId, uid, getCurrentDate(), postMessage, 0, uName, uPhotoURL, mutableListOf(), mutableListOf())
                if (uPostId == ""){
                    db.collection("Posts").add(newPost)
                            .addOnSuccessListener { documentReference ->
                                Log.w("DataBase", "updated post ID in user: ${documentReference.id}")
                                db.collection("Users").document(uid).update("postId", documentReference.id)
                                db.collection("Posts").document(documentReference.id).update("id", documentReference.id)
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

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(currentDate.time)
    }


    private fun findViewsById() {
        editPostMessage = findViewById(R.id.edit_post_message)
        sendPostButton = findViewById(R.id.FAB_send_post)
    }
}