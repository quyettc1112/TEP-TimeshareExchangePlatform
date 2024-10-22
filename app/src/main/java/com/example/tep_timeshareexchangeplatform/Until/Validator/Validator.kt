package com.example.tep_timeshareexchangeplatform.Until.Validator

class Validator {

    // Validate username
    fun validateUserName(userName: String): String? {
        return if (userName.isEmpty()) {
            "Tên người dùng không được để trống"
        } else {
            null  // Return null if valid
        }
    }

    // Validate email
    fun validateEmail(email: String): String? {
        return if (email.isEmpty()) {
            "Email không được để trống"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Email không hợp lệ"
        } else {
            null  // Return null if valid
        }
    }

    // Validate password
    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Mật khẩu không được để trống"
            password.length < 6 -> "Mật khẩu phải chứa ít nhất 6 ký tự"
            !password.matches(".*[A-Z].*".toRegex()) -> "Mật khẩu phải chứa ít nhất một chữ cái viết hoa"
            !password.matches(".*[@#\$%^&+=!].*".toRegex()) -> "Mật khẩu phải chứa ít nhất một ký tự đặc biệt"
            !password.matches(".*[0-9].*".toRegex()) -> "Mật khẩu phải chứa ít nhất một chữ số"
            else -> null  // Return null if valid
        }
    }

    // Validate phone number
    fun validatePhone(phone: String): String? {
        return if (phone.isEmpty()) {
            "Số điện thoại không được để trống"
        } else if (!phone.matches("^\\d{10}\$".toRegex())) {
            "Số điện thoại phải đúng 10 số"
        } else {
            null  // Return null if valid
        }
    }
}
