package com.example.wastemanagement.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Patterns
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

class RegisterActivity : AppCompatActivity() {
    private val scope = MainScope()
    
    private lateinit var fullNameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var termsCheckBox: CheckBox
    private lateinit var signUpButton: Button
    private lateinit var signupProgressBar: ProgressBar
    private lateinit var signupLoadingText: TextView
    private lateinit var loginPrompt: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Hide action bar
        supportActionBar?.hide()
        
        initViews()
        setupLoginPrompt()
        setupSignUpButton()
    }
    
    private fun initViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        termsCheckBox = findViewById(R.id.termsCheckBox)
        
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout)
        
        signUpButton = findViewById(R.id.signUpButton)
        signupProgressBar = findViewById(R.id.signupProgressBar)
        signupLoadingText = findViewById(R.id.signupLoadingText)
        loginPrompt = findViewById(R.id.loginPrompt)
    }
    
    private fun setupLoginPrompt() {
        val fullText = "Already have an account? Login here"
        val spannableString = SpannableString(fullText)
        
        val loginText = "Login here"
        val startIndex = fullText.indexOf(loginText)
        val endIndex = startIndex + loginText.length
        
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
        
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary_green)),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        loginPrompt.text = spannableString
        loginPrompt.movementMethod = LinkMovementMethod.getInstance()
    }
    
    private fun setupSignUpButton() {
        val authManager = AuthManager(this)
        
        signUpButton.setOnClickListener {
            if (validateInputs()) {
                performSignUp(authManager)
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        var isValid = true

        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Clear previous errors
        clearErrors()

        // Validate full name: letters and spaces only
        when {
            fullName.isEmpty() -> {
                fullNameInputLayout.error = "Full name is required"
                isValid = false
            }
            !fullName.matches(Regex("^[A-Za-z][A-Za-z ]*$")) -> {
                fullNameInputLayout.error = "Please enter a valid name"
                isValid = false
            }
        }

        // Validate email
        when {
            email.isEmpty() -> {
                emailInputLayout.error = "Email is required"
                isValid = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailInputLayout.error = "Please enter a valid email address"
                isValid = false
            }
        }

        // Validate password: 8+ chars, uppercase, lowercase, digit
        val strongPassword = password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() }

        when {
            password.isEmpty() -> {
                passwordInputLayout.error = "Password is required"
                isValid = false
            }
            !strongPassword -> {
                passwordInputLayout.error = "Password must be 8+ chars with uppercase, lowercase and digit"
                isValid = false
            }
        }

        // Validate confirm password
        when {
            confirmPassword.isEmpty() -> {
                confirmPasswordInputLayout.error = "Please confirm your password"
                isValid = false
            }
            password != confirmPassword -> {
                confirmPasswordInputLayout.error = "Passwords do not match"
                isValid = false
            }
        }

        // Validate terms acceptance
        if (!termsCheckBox.isChecked) {
            Toast.makeText(this, "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }
    
    private fun clearErrors() {
        fullNameInputLayout.error = null
        emailInputLayout.error = null
        passwordInputLayout.error = null
        confirmPasswordInputLayout.error = null
    }
    
    private fun performSignUp(authManager: AuthManager) {
        showLoading(true)
        
        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        
        scope.launch {
            try {
                val success = authManager.signup(fullName, email, password)
                
                runOnUiThread {
                    showLoading(false)
                    
                    if (success) {
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showLoading(false)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed. Please check your connection and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        signUpButton.isEnabled = !isLoading
        signupProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        signupLoadingText.visibility = if (isLoading) View.VISIBLE else View.GONE
        signupLoadingText.text = if (isLoading) "Creating your account..." else ""
    }
}
