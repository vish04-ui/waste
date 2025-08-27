package com.example.wastemanagement.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wastemanagement.ui.auth.AuthManager
import com.example.wastemanagement.ui.localization.LanguageManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    languageManager: LanguageManager,
    authManager: AuthManager
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = com.example.wastemanagement.R.string.login),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                label = { Text(stringResource(id = com.example.wastemanagement.R.string.email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                label = { Text(stringResource(id = com.example.wastemanagement.R.string.password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    error = null
                    if (email.isBlank() || password.isBlank()) {
                        error = "Please fill all fields"
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        val ok = authManager.login(email, password)
                        isLoading = false
                        if (ok) {
                            navController.navigate("dashboard") {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            error = "Invalid credentials"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = com.example.wastemanagement.ui.theme.Dimens.minTouchTarget),
                enabled = !isLoading
            ) {
                Text(
                    stringResource(id = com.example.wastemanagement.R.string.continue_label),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("${stringResource(id = com.example.wastemanagement.R.string.dont_have_account)} ${stringResource(id = com.example.wastemanagement.R.string.signup)}")
            }
        }
    }
}



