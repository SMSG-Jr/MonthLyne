package com.sergio.monthlyne

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.entity.UserInformation

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar : ProgressBar
    private lateinit var googleSignInBtn : com.google.android.gms.common.SignInButton
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth : FirebaseAuth
    private val db = Firebase.firestore

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        firebaseAuth = FirebaseAuth.getInstance()
        findViewsById()
        checkUser()

        progressBar.visibility = View.INVISIBLE

        googleSignInBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Log.d(TAG, "onCreate: begin google sign in")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: google Sign in intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e: Exception){
                Log.d(TAG, "onActivityResult: ${e.message}")
                progressBar.visibility = View.INVISIBLE
            }

        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: logged in")
                    val firebaseUser = firebaseAuth.currentUser
                    val uid = firebaseUser!!.uid
                    val email = firebaseUser.email
                    val uName = firebaseUser.displayName
                    val uPhoto = firebaseUser.photoUrl

                    Log.d(TAG, "firebaseAuthWithGoogleAccount: UID: $uid")
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: email: $email")

                    if (authResult.additionalUserInfo!!.isNewUser){
                        val userInfo = UserInformation(uid, uName.toString(), email.toString(), uPhoto.toString())
                        db.collection("Users").document(uid).set(userInfo)
                        Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created. \n$email")
                        Toast.makeText(this, "Account Created. \n$email", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User. \n$email")
                        Toast.makeText(this, "Logged In. \n$email", Toast.LENGTH_SHORT).show()
                    }
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: login failed due to ${e.message}")
                    Toast.makeText(this, "login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
    }

    private fun findViewsById() {
        googleSignInBtn = findViewById(R.id.google_sign_in_button)
        progressBar = findViewById(R.id.progressBar)
    }

}