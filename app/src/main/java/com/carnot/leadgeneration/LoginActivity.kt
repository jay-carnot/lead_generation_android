package com.carnot.leadgeneration

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUserId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    private  val authViewModel: AuthViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUserId = findViewById(R.id.editTextUserId)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)



        buttonLogin.setOnClickListener {
            val userId = editTextUserId.text.toString()
            val password = editTextPassword.text.toString()

            // Call a function to handle login
            login(userId, password)
        }
         authViewModel.isLoggedIn.observe(this){
            if (it){
                SharedPreferencesUtil.setUsername(this@LoginActivity, editTextUserId.text.toString())
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else{
                Toast.makeText(this@LoginActivity,"Try Again",Toast.LENGTH_LONG).show()
            }
         }


        authViewModel.isLoading.observe(this){
            if (it){
                ProgressHelper.showDialog(this@LoginActivity,"Verifying User")
            } else{
                ProgressHelper.dismissDialog()
            }
        }
    }

    private fun login(userId: String, password: String) {
        // Here, you can implement your login logic.
        // For demonstration purposes, we'll just display a toast message.
        val message = "User ID: $userId\nPassword: $password"
        authViewModel.login(userId,password)
    }
}
