package com.example.android.ecotown.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.android.ecotown.Models.ScreenItem
import com.example.android.ecotown.R
import kotlinx.android.synthetic.main.layout_screen.view.*

class IntroViewPagerAdapter(
    var context: Context,
    var listScreen: MutableList<ScreenItem>
) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var layoutScreen = inflater.inflate(R.layout.layout_screen, null)
        var img: ImageView = layoutScreen.intro_img
        var title: TextView = layoutScreen.intro_title
        var description: TextView = layoutScreen.intro_description

        title.text = listScreen[position].title
        description.text = listScreen[position].description
        img.setImageResource(listScreen[position].img)
        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun getCount(): Int {
        return listScreen.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}
