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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.adapter.TimelineAdapter
import com.sergio.monthlyne.entity.PostInformation
import com.sergio.monthlyne.entity.RankedPostInfo
import com.sergio.monthlyne.entity.TimelineInformation
import java.text.SimpleDateFormat
import java.util.*

class TimeLineActivity : AppCompatActivity(), TimelineAdapter.OnItemClickListener {
    private lateinit var timelineRecyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private lateinit var bottomNavigation : BottomNavigationView

    private var timelineList = emptyList<TimelineInformation>()

    private val currentDate = Calendar.getInstance()
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val timelineAdapter : TimelineAdapter = TimelineAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        findViewsById()
        setSupportActionBar(toolbar)
        bottomNavigation.setOnNavigationItemSelectedListener { itemSelected ->
            when(itemSelected.itemId){
                R.id.bottom_nav_post -> configPostButton()
                R.id.bottom_nav_profile -> configProfileButton()
            }
            true
        }

        timelineRecyclerView.apply {
            adapter = timelineAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        return dateFormat.format(currentDate.time)
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
    private fun configProfileButton() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }
    private fun configPostButton() {
        startActivity(Intent(this, FeedActivity::class.java))
        finish()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            setTimelineData()
        }
    }

    private fun setTimelineData() {
        db.collection("TimeLine").get()
                .addOnSuccessListener { result ->
                    timelineList = result.toObjects(TimelineInformation::class.java)
                    timelineAdapter.update(timelineList)
                }
                .addOnFailureListener { exception ->
                    Log.w("DataBase", "error getting document:", exception)
                }
    }

    private fun findViewsById() {
        timelineRecyclerView = findViewById(R.id.recyclerView_timeline)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }

    override fun onItemClick(position: Int) {
        val monthRankingClicked = timelineList[position]
        val intent = Intent(this, PostRankingActivity::class.java)
        intent.putExtra("Rank",monthRankingClicked.timelineId)
        startActivity(intent)
    }
}