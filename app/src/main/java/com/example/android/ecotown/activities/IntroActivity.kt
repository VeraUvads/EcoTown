package com.example.android.ecotown.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.example.android.ecotown.Adapters.IntroViewPagerAdapter
import com.example.android.ecotown.Models.ScreenItem
import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    lateinit var bindingIntro: ActivityIntroBinding
    lateinit var screenPager: ViewPager
    lateinit var introViewPagerAdapter: IntroViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        bindingIntro = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(bindingIntro.root)

        val listScreens = mutableListOf<ScreenItem>()
        listScreens.add(
            ScreenItem(
                "ghjfdghdgfd",
                "jhkdhjkhdfjkhdfjkdfh ghdgfj dgfhdghfg dhgfhjdgh",
                R.drawable.tree
            )
        )
        listScreens.add(
            ScreenItem(
                "ghjfdghdgfd",
                "jhkdhjkhdfjkhdfjkdfh ghdgfj dgfhdghfg dhgfhjdgh",
                R.drawable.trash
            )
        )
        listScreens.add(
            ScreenItem(
                "ghjfdghdgfd",
                "jhkdhjkhdfjkhdfjkdfh ghdgfj dgfhdghfg dhgfhjdgh",
                R.drawable.lung
            )
        )

        screenPager = bindingIntro.screenViewpager
        introViewPagerAdapter = IntroViewPagerAdapter(this, listScreens)
        screenPager.adapter = introViewPagerAdapter
        bindingIntro.btnNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        bindingIntro.tabIndicator.setupWithViewPager(screenPager)  //свяжет данный ViewPager и этот TabLayout вместе, чтобы изменения в одном автоматически отражались в другом



    }
}
