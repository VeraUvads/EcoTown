package com.example.android.ecotown.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.ecotown.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.progressBar.visibility = View.INVISIBLE

        binding.regButton.setOnClickListener {
            binding.regButton.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            var name: String = binding.regName.text.toString()
            var email: String = binding.regEmail.text.toString()
            var password: String = binding.regPassword.text.toString()
            var confirm: String = binding.regConfirm.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password != confirm) {

                binding.progressBar.visibility = View.INVISIBLE
                binding.regButton.visibility = View.VISIBLE
                makeText("Все поля должны быть заполнены")
            } else {
                createUserAccount(email, name, password)
            }


        }

    }

    private fun createUserAccount(email: String, name: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("TAG", "createUserWithEmail:success")
                val user = mAuth.currentUser
                makeText("Аккаунт создан")
                if (user != null) {
                    updateUserInfo(name, user)
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.i("TAG", "createUserWithEmail:failure", it.exception)
                makeText("Ошибка аутентификации")
                binding.progressBar.visibility = View.INVISIBLE
                binding.regButton.visibility = View.VISIBLE
            }
        }

    }

    private fun updateUserInfo(name: String, currentUser: FirebaseUser) {
        val profileUpdate: UserProfileChangeRequest =
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

        currentUser.updateProfile(profileUpdate)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    makeText("Регистрация завершена")
                    updateUI()
                }
            }
    }

    private fun updateUI() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun makeText(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }


}
