package com.example.android.ecotown.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.ecotown.Adapters.CheckListAdapter
import com.example.android.ecotown.Models.Check
import com.example.android.ecotown.databinding.ActivityCheckListBinding
import com.google.firebase.database.*

class CheckListActivity : AppCompatActivity() {
    private lateinit var bindingCheckList: ActivityCheckListBinding

    private var checkList = mutableListOf<Check>()

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    val firstKey: String = "1HWplH_zdri52-grE2_Jy7LtXi5uHAvqJHm12Z8Tu6OM"
    val secondKey: String = "Лист1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingCheckList = ActivityCheckListBinding.inflate(layoutInflater)
        setContentView(bindingCheckList.root)


        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference(firstKey).child(secondKey)


        val layoutManager = LinearLayoutManager(applicationContext)
        bindingCheckList.recycler.layoutManager = layoutManager


        val checkListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                checkList = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val idString= postSnap.child("id").value.toString()
                    val id = idString.toIntOrNull()
                    val item = postSnap.child("item").value.toString()
                    val statePost = postSnap.child("state").value.toString()
                    val state = statePost.toBoolean()

                    val check = Check(id!!, item, state)
                    checkList.add(check)
                }
                val checkAdapter = CheckListAdapter(checkList)
                bindingCheckList.recycler.adapter = checkAdapter
            }

        }
        databaseReference.addValueEventListener(checkListener)
    }
}
