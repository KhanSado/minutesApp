package io.github.bersoncrios.minutesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.bersoncrios.minutesapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnSignout.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            loginWithEmailPass(
                binding.etEmail.text.toString().trim(),
                binding.etPass.text.toString().trim()
            )
        }

        auth = Firebase.auth
    }

    private fun loginWithEmailPass(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    var userApp: CustomApplication = CustomApplication.instance!!
                    userApp.userId = auth.currentUser!!.uid
                    userApp.username = auth.currentUser!!.displayName
                    goToHome()
                } else {
                    Toast.makeText(this, "Login Failure", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToHome()
        }
    }

    private fun goToHome() {
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}