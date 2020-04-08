package com.example.android.ecotown.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.ecotown.Adapters.CheckListAdapter
import com.example.android.ecotown.Models.ChangedData
import com.example.android.ecotown.Models.Check
import com.example.android.ecotown.databinding.ActivityCheckListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class CheckListActivity : AppCompatActivity() {
    private lateinit var bindingCheckList: ActivityCheckListBinding

    lateinit var checkList: MutableList<Check>
    lateinit var changedDataList: MutableList<ChangedData>

    lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var databaseChangedData: DatabaseReference

    lateinit var currentUser: FirebaseUser

    lateinit var checkAdapter: CheckListAdapter

    val firstKey: String = "1HWplH_zdri52-grE2_Jy7LtXi5uHAvqJHm12Z8Tu6OM"
    val secondKey: String = "Лист1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingCheckList = ActivityCheckListBinding.inflate(layoutInflater)
        setContentView(bindingCheckList.root)


        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!


        databaseReference = database.getReference(firstKey).child(secondKey)
        databaseChangedData = database.getReference(currentUser.uid + "Habit")

        val layoutManager = LinearLayoutManager(applicationContext)
        bindingCheckList.recycler.layoutManager = layoutManager


        val checkListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                checkList = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val idString = postSnap.child("id").value.toString()
                    val id = idString.toIntOrNull()
                    val item = postSnap.child("item").value.toString()
                    val statePost = postSnap.child("state").value.toString()
                    val state = statePost.toBoolean()
                    val check = Check(id!!, item, state)
                    checkList.add(check)
                }

                checkAdapter = CheckListAdapter(checkList)
                bindingCheckList.recycler.adapter = checkAdapter //адаптер

            }

//                    val idChanged = postSnap.child("habitId").value.toString()
//                    val stateChanged  = postSnap.child("state").value.toString().toBoolean()

//                    val changedData = ChangedData(idChanged, stateChanged)
//                    changedDataList.add(changedData)

//
//                }if (changedDataList.isNotEmpty()){
////                for (i in changedDataList){
////                    for (n in checkList){
////                    if(n.id.equals(i.habitId)){
////                        n.state = i.state
////                    }
////                    }
////                }


        }
        val changeListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                changedDataList = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val idChanged = postSnap.child("habitId").value.toString()
                    val stateChanged = postSnap.child("state").value.toString().toBoolean()

                    val changedData = ChangedData(idChanged, stateChanged)
                    changedDataList.add(changedData)


                }
                listCompare()
                bindingCheckList.recycler.adapter?.notifyDataSetChanged()
            }
        }

        databaseReference.addValueEventListener(checkListener)
        databaseChangedData.addValueEventListener(changeListener)


    }

    private fun listCompare() { //TODO переписать

        if (changedDataList.isNotEmpty()) {

            for (i in changedDataList) {
                for (n in checkList) {
                    if (n.id.toString() == i.habitId) {
                        n.state = i.state
                    }
                }
            }
        }
    }

}
