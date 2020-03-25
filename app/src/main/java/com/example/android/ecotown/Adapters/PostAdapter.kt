package com.example.android.ecotown.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.ecotown.Models.Post
import com.example.android.ecotown.R
import com.example.android.ecotown.activities.PostDetailActivity
import com.example.android.ecotown.databinding.PostItemBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.comment_item.view.*

class PostAdapter(private var mData: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    private val user = FirebaseAuth.getInstance().currentUser
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row: View =
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false) //check
        context = parent.context
        return MyViewHolder(row)
    }

    override fun getItemCount(): Int {

        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = mData[position].title
        holder.description.text = mData[position].description
        holder.userName.text = mData[position].userName
        Glide.with(context).load(mData[position].picture).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra("title", mData[position].title)
            intent.putExtra("description", mData[position].description)
            intent.putExtra("postKey", mData[position].postKey)
            intent.putExtra("postImage", mData[position].picture)
            intent.putExtra("postName", mData[position].userName)
            val time: Long= mData[position].timeStamp as Long
            intent.putExtra("time",  time)
            context.startActivity(intent)
        }


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.cardTitle)
        val description: TextView = itemView.findViewById(R.id.cardDescription)
        val image: ImageView = itemView.findViewById(R.id.cardImage)
        val userName: TextView = itemView.findViewById(R.id.cardName)




    }
}