package com.sergio.monthlyne.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.adapter.TimelineAdapter
import com.sergio.monthlyne.entity.TimelineInformation

class TimeLineActivity : AppCompatActivity(), TimelineAdapter.OnItemClickListener {
    private lateinit var timelineRecyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private var timelineList = emptyList<TimelineInformation>()

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private val timelineAdapter : TimelineAdapter = TimelineAdapter(this)

    // TODO: 05/10/2021 Testing: add button to create a month ranking of posts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        findViewsById()
        setSupportActionBar(toolbar)
        timelineRecyclerView.apply {
            adapter = timelineAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        checkUser()
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
    }

    override fun onItemClick(position: Int) {
        val monthRankingClicked = timelineList[position]
        val intent = Intent(this, PostRankingActivity::class.java)
        intent.putExtra("Rank",monthRankingClicked.timelineId)
        startActivity(intent)
    }
}