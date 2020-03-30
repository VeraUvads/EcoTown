package com.example.android.ecotown.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecotown.Models.Check
import com.example.android.ecotown.R
import kotlinx.android.synthetic.main.check_list_item.view.*

class CheckListAdapter(private var checkList: MutableList<Check>) :
    RecyclerView.Adapter<CheckListAdapter.MyHolderView>() {

    lateinit var context: Context
    lateinit var changeList: MutableList<Check>

    private val _ischecked = MutableLiveData<Boolean>()
    val ischecked: LiveData<Boolean>
        get() = _ischecked

    var pos = 0   //ПОЧЕМУ НЕ ПЕРЕЗАПИСЫВАЕТСЯ, ОБРАЩЕНИЕ ИДЕТ В ПЕРВЫЙ РАЗ К 0 ЭЛЕМЕНТУ
    var text = ""


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
        holder.card.animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)
        holder.checkBox.animation =
            AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation)

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->   //ПОЧЧЕМУ РЕАГИРУЕТ ТОЛЬКО НА ВТОРОЕ НАЖАТИЕ
            _ischecked.value = isChecked
            pos = position
            text = checkList[position].item
        }

    }

    class MyHolderView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxList)
        val card = itemView.checkBoxCard
    }

}