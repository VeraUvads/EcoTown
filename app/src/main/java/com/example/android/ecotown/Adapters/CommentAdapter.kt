package com.example.android.ecotown.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecotown.Models.Comment
import com.example.android.ecotown.R
import kotlinx.android.synthetic.main.comment_item.view.*
import java.text.SimpleDateFormat

class CommentAdapter(private var comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_item, parent, false)
        context = parent.context
        return MyViewHolder(row)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.text= comments[position].uName
        holder.tvContent.text =  comments[position].content
        holder.tvDate.text = timeStampToString(comments[position].timeStamp as Long)
        holder.commentCard.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)
        holder.tvName.animation =
            AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.comment_username)
        val tvContent: TextView = itemView.findViewById(R.id.comment_content)
        val tvDate: TextView = itemView.findViewById(R.id.comment_date)

        val commentCard: ConstraintLayout = itemView.comment_card


    }


    private fun timeStampToString(s: Long?): String {
        val format = SimpleDateFormat("MM-dd-yyyy")
        return format.format(s)
    }
}
