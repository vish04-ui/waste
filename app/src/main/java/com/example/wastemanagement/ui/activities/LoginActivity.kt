package com.example.wastemanagement.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.auth.AuthManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val scope = MainScope()
    
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var loginLoadingText: TextView
    private lateinit var signUpPrompt: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        initViews()
        setupLoginButton()
        setupSignUpPrompt()
    }
    
    private fun initViews() {
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        loginProgressBar = findViewById(R.id.loginProgressBar)
        loginLoadingText = findViewById(R.id.loginLoadingText)
        signUpPrompt = findViewById(R.id.signUpPrompt)
    }
    
    private fun setupLoginButton() {
        val authManager = AuthManager(this)
        
        loginButton.setOnClickListener {
            clearErrors()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            
            if (validateForm(email, password)) {
                performLogin(authManager, email, password)
            }
        }
    }
    
    private fun setupSignUpPrompt() {
        val fullText = "Don't have an account? Sign up"
        val spannableString = SpannableString(fullText)
        val signUpStart = fullText.indexOf("Sign up")
        val signUpEnd = signUpStart + "Sign up".length
        
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        
        spannableString.setSpan(
            clickableSpan,
            signUpStart,
            signUpEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_green)),
            signUpStart,
            signUpEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        signUpPrompt.text = spannableString
        signUpPrompt.movementMethod = LinkMovementMethod.getInstance()
    }
    
    private fun validateForm(email: String, password: String): Boolean {
        var isValid = true
        
        when {
            email.isEmpty() -> {
                emailInputLayout.error = "Email is required"
                isValid = false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailInputLayout.error = "Please enter a valid email address"
                isValid = false
            }
        }
        
        when {
            password.isEmpty() -> {
                passwordInputLayout.error = "Password is required"
                isValid = false
            }
            password.length < 6 -> {
                passwordInputLayout.error = "Password must be at least 6 characters"
                isValid = false
            }
        }
        
        return isValid
    }
    
    private fun clearErrors() {
        emailInputLayout.error = null
        passwordInputLayout.error = null
    }
    
    private fun performLogin(authManager: AuthManager, email: String, password: String) {
        showLoading(true)
        
        scope.launch {
            try {
                val success = authManager.login(email, password)
                
                runOnUiThread {
                    showLoading(false)
                    
                    if (success) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Invalid email or password. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showLoading(false)
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed. Please check your connection and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        loginButton.isEnabled = !isLoading
        loginProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        loginLoadingText.visibility = if (isLoading) View.VISIBLE else View.GONE
        loginLoadingText.text = if (isLoading) "Signing you in..." else ""
    }
}
