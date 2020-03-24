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

    private val checkList = mutableListOf<Check>()

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingCheckList = ActivityCheckListBinding.inflate(layoutInflater)
        setContentView(bindingCheckList.root)


        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Check")


         val layoutManager = LinearLayoutManager(applicationContext)
         bindingCheckList.recycler.layoutManager = layoutManager


        val checkListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnap: DataSnapshot in p0.children) {
                    val check = postSnap.getValue(Check::class.java)
                    checkList.add(check!!)
                }
                val checkAdapter = CheckListAdapter(checkList)
                bindingCheckList.recycler.adapter = checkAdapter
            }

        }

        databaseReference.addValueEventListener(checkListener)
    }
}
