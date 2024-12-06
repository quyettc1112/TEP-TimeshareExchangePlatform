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

    fun validatePrice(price: String?): String? {
        return if (price.isNullOrEmpty()) {
            "Giá không được để trống"
        } else {
            try {
                // Loại bỏ các ký tự không phải số, bao gồm cả ký tự "đ"
                val cleanedPrice = price.replace("[^\\d.]".toRegex(), "")

                val priceValue = cleanedPrice.toDouble()
                when {
                    priceValue <= 0 -> "Giá phải lớn hơn 0"
                    priceValue > 10_000_000_000 -> "Giá không được vượt quá 10 tỷ"
                    else -> null  // Return null if valid
                }
            } catch (e: NumberFormatException) {
                "Giá không hợp lệ"
            }
        }
    }

    // Validate confirm password
    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "Xác nhận mật khẩu không được để trống"
            confirmPassword != password -> "Xác nhận mật khẩu không khớp"
            else -> null  // Return null if valid
        }
    }
}
