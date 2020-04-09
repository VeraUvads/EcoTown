package com.example.android.ecotown.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecotown.Models.ChangedData
import com.example.android.ecotown.Models.Check
import com.example.android.ecotown.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.check_list_item.view.*

class CheckListAdapter(private var checkList: MutableList<Check>) :
    RecyclerView.Adapter<CheckListAdapter.MyHolderView>() {

    lateinit var context: Context
    lateinit var listChanged: MutableList<ChangedData>

    lateinit var database: FirebaseDatabase
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()   //TODO переписать

    lateinit var referenceChangedData: DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val row: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.check_list_item, parent, false)
        context = parent.context
        return MyHolderView(row)
    }

    override fun getItemCount(): Int {
        return checkList.size
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
        holder.checkBox.isChecked = checkList[position].state
        holder.checkBox.text = checkList[position].item
        holder.checkBox.setOnClickListener {

                    addChangedData(checkList[position].id, holder.checkBox.isChecked)


        }

    }

    private fun addChangedData(id: Int, checked: Boolean) {
        database = FirebaseDatabase.getInstance()
        referenceChangedData = database.getReference(currentUserId + "Habit").push()
        referenceChangedData.setValue(ChangedData(id.toString(), checked))
            .addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(this.context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
            }
    }


    class MyHolderView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxList)

    }

}