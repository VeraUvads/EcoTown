package com.example.android.ecotown.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
        bindingIntro = ActivityIntroBinding.inflate(layoutInflater)

        // на полный экран
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (restorePrefData()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        setContentView(bindingIntro.root)

        supportActionBar?.hide()

        val listScreens = mutableListOf<ScreenItem>()
        listScreens.add(
            ScreenItem(
                "Чек-лист эко-привычек",
                "Экологичный образ жизни помогает не только беречь окружающую среду, но и экономить",
                R.drawable.tree
            )
        )
        listScreens.add(
            ScreenItem(
                "Делись с друзьями",
                "Находи полезные лайф-хаки и обсуждай их со своими единомышленниками",
                R.drawable.lung
            )
        )
        listScreens.add(
            ScreenItem(
                "Карта",
                "Ищи ближайшие пункты сортировки отходов, эко-магазины и много другое",
                R.drawable.trash
            )
        )

        screenPager = bindingIntro.screenViewpager
        introViewPagerAdapter = IntroViewPagerAdapter(this, listScreens)
        screenPager.adapter = introViewPagerAdapter
        bindingIntro.btnNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        bindingIntro.tabIndicator.setupWithViewPager(screenPager) //свяжет  ViewPager и TabLayout вместе, чтобы изменения в одном автоматически отражались в другом


        savePrefData()

    }

    private fun restorePrefData(): Boolean {
        val pref: SharedPreferences =
            applicationContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return pref.getBoolean("isIntroOpened", false)
    }

    private fun savePrefData() {
        val pref: SharedPreferences =
            applicationContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putBoolean("isIntroOpened", true)
        editor.apply()

    }
}
