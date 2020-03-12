package com.example.android.ecotown.activities

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.ActivityHomeBinding
import com.example.android.ecotown.databinding.ContentMainBinding
import com.example.android.ecotown.databinding.NavHeaderBinding
import com.example.android.ecotown.fragment.HomeFragment
import com.example.android.ecotown.fragment.TrackFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.nav_header.view.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var bindingHome: ActivityHomeBinding
    private lateinit var bindingContent: ContentMainBinding

    lateinit var mAuth: FirebaseAuth

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingHome = ActivityHomeBinding.inflate(layoutInflater)
        bindingContent = ContentMainBinding.inflate(layoutInflater)

        setContentView(bindingHome.root)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth?.currentUser
        navHeader(currentUser)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment()).commit()

        drawerLayout = bindingHome.drawerLayout
        navView = bindingHome.navView
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun navHeader(currentUser: FirebaseUser?) {
        val navView: NavigationView = bindingHome.navView
        val headerView = navView.getHeaderView(0)
        val navName = headerView.nav_user_name
        val navMail = headerView.nav_user_mail

        navMail.text = currentUser?.email
        navName.text = currentUser?.displayName

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        supportActionBar?.title = item.title
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment()).commit()

            }
            R.id.nav_track -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, TrackFragment()).commit()
            }
            R.id.nav_map -> {
            }
            R.id.nav_settings -> {
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
