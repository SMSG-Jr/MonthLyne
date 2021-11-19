package com.sergio.monthlyne.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sergio.monthlyne.R
import com.sergio.monthlyne.adapter.RankedPostAdapter
import com.sergio.monthlyne.entity.RankedPostInfo
import com.sergio.monthlyne.entity.TimelineInformation

class PostRankingActivity : AppCompatActivity() {
    private lateinit var rankedRecyclerView : RecyclerView
    private lateinit var monthRankId : String
    private var rankedList = emptyList<RankedPostInfo>()

    private val db = Firebase.firestore

    private val rankedAdapter : RankedPostAdapter = RankedPostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_ranking)

        monthRankId = intent.getSerializableExtra("Rank") as String

        findViewsById()
        rankedRecyclerView.apply {
            adapter = rankedAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        setRankData()
    }

    private fun findViewsById() {
        rankedRecyclerView = findViewById(R.id.recyclerView_post_ranking)
    }
    private fun setRankData() {
        db.collection("TimeLine").document(monthRankId).collection("Post Ranking").orderBy("rankedPostRank").get()
            .addOnSuccessListener { result ->
                rankedList = result.toObjects(RankedPostInfo::class.java)
                rankedAdapter.update(rankedList)
            }
            .addOnFailureListener { exception ->
                Log.w("DataBase", "error getting document:", exception)
            }
    }


}