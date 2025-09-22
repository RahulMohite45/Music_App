package com.example.mymusicappui.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Edit


@Composable
fun AccountView() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPrefs = remember {
        context.getSharedPreferences("account_details", Context.MODE_PRIVATE)
    }

    // State variables
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var musicTaste by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSavedDetails by remember { mutableStateOf(false) }
    var saveSuccess by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }

    // OTP related states
    var showOtpDialog by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isVerifyingOtp by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }
    var otpTimer by remember { mutableStateOf(0) }
    var canResendOtp by remember { mutableStateOf(true) }

    // Load saved data when screen opens
    LaunchedEffect(Unit) {
        name = sharedPrefs.getString("name", "") ?: ""
        email = sharedPrefs.getString("email", "") ?: ""
        phone = sharedPrefs.getString("phone", "") ?: ""
        age = sharedPrefs.getString("age", "") ?: ""
        musicTaste = sharedPrefs.getString("music_taste", "") ?: ""
        isPhoneVerified = sharedPrefs.getBoolean("phone_verified", false)

        // Check if data exists to show saved details and hide form
        if (name.isNotEmpty() || email.isNotEmpty() || age.isNotEmpty() || musicTaste.isNotEmpty()) {
            showSavedDetails = true
            showForm = false
        } else {
            showForm = true
        }
    }

    // OTP Timer effect
    LaunchedEffect(otpTimer) {
        if (otpTimer > 0) {
            delay(1000)
            otpTimer--
        } else {
            canResendOtp = true
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Account Details",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Save Success Message
        if (saveSuccess) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditing) "Changes saved successfully!" else "Details saved successfully!",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Save Button at the top (Only show when form is visible)
        if (showForm) {
            Button(
                onClick = {
                    if (!isPhoneVerified && phone.isNotBlank()) {
                        // Generate and send OTP first
                        generatedOtp = generateOtp()
                        sendOtp(phone, generatedOtp)
                        showOtpDialog = true
                        otpTimer = 30
                        canResendOtp = false
                    } else {
                        // Save details directly if phone is verified or empty
                        isLoading = true
                        saveAccountDetails(sharedPrefs, name, email, phone, age, musicTaste, isPhoneVerified)

                        coroutineScope.launch {
                            delay(1000)
                            isLoading = false
                            saveSuccess = true
                            showSavedDetails = true
                            showForm = false
                            isEditing = false

                            delay(3000)
                            saveSuccess = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = name.isNotBlank() && email.isNotBlank() && age.isNotBlank() && musicTaste.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = when {
                            !isPhoneVerified && phone.isNotBlank() -> "Verify Phone & Save"
                            isEditing -> "Update Details"
                            else -> "Save Details"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Form Fields Section (Only show when showForm is true)
        if (showForm) {
            Spacer(modifier = Modifier.height(8.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Name"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Phone Field
            OutlinedTextField(
                value = phone,
                onValueChange = { newValue ->
                    // Only allow numeric input and limit to 10 digits
                    if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.length <= 10)) {
                        phone = newValue
                        // Reset verification status when phone changes
                        if (phone != sharedPrefs.getString("phone", "")) {
                            isPhoneVerified = false
                        }
                    }
                },
                label = { Text("Phone Number") },
                placeholder = { Text("10-digit mobile number") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Phone,
                        contentDescription = "Phone"
                    )
                },
                trailingIcon = {
                    if (isPhoneVerified) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isPhoneVerified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = if (isPhoneVerified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            )

            // Phone verification status
            if (phone.isNotBlank()) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isPhoneVerified) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                        contentDescription = null,
                        tint = if (isPhoneVerified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPhoneVerified) "Phone number verified" else "Phone number needs verification",
                        fontSize = 12.sp,
                        color = if (isPhoneVerified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            // Age Field
            OutlinedTextField(
                value = age,
                onValueChange = { newValue ->
                    // Only allow numeric input
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        age = newValue
                    }
                },
                label = { Text("Age") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Cake,
                        contentDescription = "Age"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Music Taste Field
            OutlinedTextField(
                value = musicTaste,
                onValueChange = { musicTaste = it },
                label = { Text("Music Taste") },
                placeholder = { Text("e.g., Rock, Pop, Hip Hop, Jazz...") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.MusicNote,
                        contentDescription = "Music Taste"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Cancel Edit Button (only show when editing)
            if (isEditing) {
                OutlinedButton(
                    onClick = {
                        // Reset form values to saved values
                        coroutineScope.launch {
                            name = sharedPrefs.getString("name", "") ?: ""
                            email = sharedPrefs.getString("email", "") ?: ""
                            phone = sharedPrefs.getString("phone", "") ?: ""
                            age = sharedPrefs.getString("age", "") ?: ""
                            musicTaste = sharedPrefs.getString("music_taste", "") ?: ""
                            isPhoneVerified = sharedPrefs.getBoolean("phone_verified", false)

                            showForm = false
                            showSavedDetails = true
                            isEditing = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancel")
                }
            }
        }

        // Saved Details Display Section (Always show if data exists)
        if (showSavedDetails && (name.isNotEmpty() || email.isNotEmpty() || age.isNotEmpty() || musicTaste.isNotEmpty())) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header with Edit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saved Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = {
                        isEditing = true
                        showForm = true
                        showSavedDetails = false
                    }
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit Details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            SavedDetailsCard(
                name = name,
                email = email,
                phone = phone,
                age = age,
                musicTaste = musicTaste,
                isPhoneVerified = isPhoneVerified
            )
        }

        // Info Card (Always visible)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "💡 Tip",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your details are saved locally and will persist even after closing the app. Phone verification helps secure your account and enables SMS notifications for new playlists!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // OTP Verification Dialog
    if (showOtpDialog) {
        OtpVerificationDialog(
            otpCode = otpCode,
            onOtpChange = { otpCode = it },
            onVerify = {
                isVerifyingOtp = true
                otpError = ""
                coroutineScope.launch {
                    delay(1000) // Simulate verification
                    if (otpCode == generatedOtp) {
                        isPhoneVerified = true
                        showOtpDialog = false
                        otpCode = ""
                        // Save account details after successful OTP verification
                        delay(500)
                        isLoading = true
                        saveAccountDetails(sharedPrefs, name, email, phone, age, musicTaste, isPhoneVerified)

                        delay(1000)
                        isLoading = false
                        saveSuccess = true
                        showSavedDetails = true
                        showForm = false
                        isEditing = false

                        delay(3000)
                        saveSuccess = false
                    } else {
                        otpError = "Invalid OTP. Please try again."
                    }
                    isVerifyingOtp = false
                }
            },
            onResendOtp = {
                if (canResendOtp) {
                    generatedOtp = generateOtp()
                    sendOtp(phone, generatedOtp)
                    otpTimer = 30
                    canResendOtp = false
                    otpError = ""
                }
            },
            onDismiss = {
                showOtpDialog = false
                otpCode = ""
                otpError = ""
            },
            isVerifying = isVerifyingOtp,
            error = otpError,
            phoneNumber = phone,
            timerSeconds = otpTimer,
            canResend = canResendOtp
        )
    }
}

@Composable
fun OtpVerificationDialog(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResendOtp: () -> Unit,
    onDismiss: () -> Unit,
    isVerifying: Boolean,
    error: String,
    phoneNumber: String,
    timerSeconds: Int,
    canResend: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Verify Phone Number",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter the 6-digit OTP sent to +91 $phoneNumber",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { newValue ->
                        if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                            onOtpChange(newValue)
                        }
                    },
                    label = { Text("OTP Code") },
                    placeholder = { Text("123456") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = error.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onResendOtp,
                        enabled = canResend
                    ) {
                        Text(
                            text = if (canResend) "Resend OTP" else "Resend in ${timerSeconds}s"
                        )
                    }

                    Text(
                        text = "Demo OTP: generateOtp", // Remove this in production!
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onVerify,
                enabled = otpCode.length == 6 && !isVerifying
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Verify")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SavedDetailsCard(
    name: String,
    email: String,
    phone: String,
    age: String,
    musicTaste: String,
    isPhoneVerified: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Name
            DetailRow(
                icon = Icons.Filled.Person,
                label = "Name",
                value = name
            )

            // Email
            DetailRow(
                icon = Icons.Filled.Email,
                label = "Email",
                value = email
            )

            // Phone
            if (phone.isNotBlank()) {
                DetailRowWithVerification(
                    icon = Icons.Filled.Phone,
                    label = "Phone",
                    value = "+91 $phone",
                    isVerified = isPhoneVerified
                )
            }

            // Age
            DetailRow(
                icon = Icons.Filled.Cake,
                label = "Age",
                value = "$age years old"
            )

            // Music Taste
            DetailRow(
                icon = Icons.Filled.MusicNote,
                label = "Music Taste",
                value = musicTaste
            )
        }
    }
}

@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun DetailRowWithVerification(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isVerified: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
        }
        if (isVerified) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Verified",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Verified",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

// Data class for account details
data class AccountDetails(
    val name: String,
    val email: String,
    val phone: String,
    val age: Int,
    val musicTaste: String,
    val isPhoneVerified: Boolean
)

// Function to save account details to SharedPreferences
private fun saveAccountDetails(
    sharedPrefs: SharedPreferences,
    name: String,
    email: String,
    phone: String,
    age: String,
    musicTaste: String,
    isPhoneVerified: Boolean
) {
    with(sharedPrefs.edit()) {
        putString("name", name)
        putString("email", email)
        putString("phone", phone)
        putString("age", age)
        putString("music_taste", musicTaste)
        putBoolean("phone_verified", isPhoneVerified)
        putLong("save_timestamp", System.currentTimeMillis())
        apply()
    }
}

// Function to generate OTP (Demo - replace with real service)
private fun generateOtp(): String {
    return (100000..999999).random().toString()
}

// Function to send OTP (Demo - replace with SMS service)
private fun sendOtp(phoneNumber: String, otp: String) {
    // In a real app, integrate with SMS service like:
    // - Firebase Auth Phone Verification
    // - AWS SNS
    // - Twilio
    // - MSG91
    // - TextLocal

    println("Sending OTP $otp to +91 $phoneNumber") // Demo log
}

// Function to load saved details (optional utility function)
fun loadAccountDetails(context: Context): AccountDetails? {
    val sharedPrefs = context.getSharedPreferences("account_details", Context.MODE_PRIVATE)

    val name = sharedPrefs.getString("name", "") ?: ""
    val email = sharedPrefs.getString("email", "") ?: ""
    val phone = sharedPrefs.getString("phone", "") ?: ""
    val age = sharedPrefs.getString("age", "") ?: ""
    val musicTaste = sharedPrefs.getString("music_taste", "") ?: ""
    val isPhoneVerified = sharedPrefs.getBoolean("phone_verified", false)

    return if (name.isNotEmpty() && email.isNotEmpty()) {
        AccountDetails(
            name = name,
            email = email,
            phone = phone,
            age = age.toIntOrNull() ?: 0,
            musicTaste = musicTaste,
            isPhoneVerified = isPhoneVerified
        )
    } else {
        null
    }
}