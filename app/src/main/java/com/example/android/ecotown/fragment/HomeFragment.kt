package com.example.android.ecotown.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecotown.Adapters.PostAdapter
import com.example.android.ecotown.Models.Post
import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var mAuth: FirebaseAuth

    lateinit var bindingHomeFragment: FragmentHomeBinding

    lateinit var postRecyclerView: RecyclerView

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    lateinit var postList: MutableList<Post>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingHomeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        postRecyclerView = bindingHomeFragment.postRecycler
        postRecyclerView.layoutManager = LinearLayoutManager(activity)
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Posts")

        return bindingHomeFragment.root
    }


    override fun onStart() {

        mAuth = FirebaseAuth.getInstance()



        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                postList = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val post = postSnap.getValue(Post::class.java)
                    postList.add(post as Post)
                }
                val postAdapter = PostAdapter(postList)
                postRecyclerView.adapter = postAdapter
                if (postAdapter.itemCount > 0){
                    bindingHomeFragment.greeting.visibility = View.INVISIBLE
                }
            }

        }



        databaseReference.addValueEventListener(postListener)

        super.onStart()
    }


}
