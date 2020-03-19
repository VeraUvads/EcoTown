package com.example.android.ecotown.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.android.ecotown.Models.Post
import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.*
import com.example.android.ecotown.fragment.HomeFragment
import com.example.android.ecotown.fragment.TrackFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.popup.view.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var bindingHome: ActivityHomeBinding
    private lateinit var bindingContent: ContentMainBinding
    private lateinit var bindingPopUp: PopupBinding
    private lateinit var bindingHomeFragment: FragmentHomeBinding

    lateinit var mAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var popUp: Dialog

    private lateinit var bar: ProgressBar

    private val pReqCode = 2;
    private val requestCode = 2

    private lateinit var pickedImgUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingHome = ActivityHomeBinding.inflate(layoutInflater)
        bindingContent = ContentMainBinding.inflate(layoutInflater)
        bindingHomeFragment = FragmentHomeBinding.inflate(layoutInflater)

        setContentView(bindingHome.root)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        navHeader(currentUser)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
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

    fun iniPop(view: View) {
        popUp = Dialog(this)
        bindingPopUp = PopupBinding.inflate(layoutInflater)
        popUp.setContentView(bindingPopUp.root)
        popUp.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popUp.window
            ?.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popUp.window?.attributes?.gravity = Gravity.TOP
        bar = bindingPopUp.progressBarAdd
        popUp.show()


    }

    fun checkAndRequestForPermission(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

                Toast.makeText(this, "Пожалуйста настройте доступ к медиа", Toast.LENGTH_SHORT)
                    .show();

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    pReqCode
                )
            }

        } else
            openGallery()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.type = "image/*";
        startActivityForResult(galleryIntent, requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == this.requestCode && data != null) {

            pickedImgUri = data.data!!
            bindingPopUp.addPicture.setImageURI(pickedImgUri);

        }
    }


    fun addingButton(view: View) {

        view.visibility = View.INVISIBLE
        bar.visibility = View.VISIBLE

        if (bindingPopUp.description.text.toString().isEmpty() && bindingPopUp.title.text.toString().isEmpty()) {
            Toast.makeText(this, "Пожалуйста заполните поля", Toast.LENGTH_SHORT).show()
            view.visibility = View.VISIBLE
            bar.visibility = View.INVISIBLE
        } else {
            val storageReference = FirebaseStorage.getInstance().reference.child("user_images")
            val imagePathFile: StorageReference =
                storageReference.child(pickedImgUri.lastPathSegment.toString())

            imagePathFile.putFile(pickedImgUri).addOnSuccessListener {
                imagePathFile.downloadUrl.addOnSuccessListener{
                    val imageDownloadLink : String = it.toString()
                    val post = Post(bindingPopUp.title.text.toString(), bindingPopUp.title.description.toString(),  imageDownloadLink,
                        currentUser.uid,
                        currentUser.photoUrl.toString())

                    addPost(post)
                }
            }



        }


    }

    private fun addPost(post: Post) {

    }



}
