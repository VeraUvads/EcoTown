package com.example.android.ecotown.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.android.ecotown.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.INVISIBLE
        mAuth = FirebaseAuth.getInstance()

        binding.logButton.setOnClickListener {
            binding.logButton.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            var email: String = binding.logEmail.text.toString()
            var password: String = binding.logPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.logButton.visibility = View.VISIBLE
                makeText("Все поля должны быть заполнены")

            } else {

                signIn(email, password)
            }
        }

        binding.textRegister.setOnClickListener {
            val intentRegister = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intentRegister)
        }


    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                binding.logButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                if (it.isSuccessful){
                    updateUI()
                } else{
                    makeText("Неверный логин или пароль")
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = mAuth.currentUser
        if (user != null){
            updateUI()
        }
    }

    private fun updateUI() {
        val intentHome = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intentHome)
    }

    private fun makeText(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
}
