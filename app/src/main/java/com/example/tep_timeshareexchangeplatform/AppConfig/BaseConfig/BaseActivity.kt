package com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ConfirmDialog
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.ErrorDialog
import com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomDialog.NotifyDialog
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.PreferenceHelper
import java.util.Locale

open class  BaseActivity : AppCompatActivity() {

    var progressDialog: ProgressDialog? = null

    open fun showLoading(
        isShow: Boolean
    ) {

    }

    open fun goBackActivity(context: Context, nextActivity: Class<out Activity>) {
        val intent = Intent(context, nextActivity)
        context.startActivity(intent)
        (context as Activity).finish()
    }


    open fun showLoading(
        title: String,
        message: String,
        cancelable: Boolean = true,
        cancelListener: ((DialogInterface) -> Unit) = {}
    ) {
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle(title)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(cancelable)

        if (cancelable) {
            progressDialog?.setOnCancelListener(cancelListener)
        }

        progressDialog?.show()

    }

    open fun hideLoading() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    open fun showErrorDialog(message: String, textButton: String?) {
        val errorDialog = ErrorDialog(this, message, textButton)
        errorDialog.show()
        errorDialog.window?.setGravity(Gravity.CENTER)
        errorDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showNotifyDialog(
        titleResourceId: Int,
        messageResourceId: Int,
        textButtonResourceId: Int = -1
    ) {
        val title = getString(titleResourceId)
        val message = getString(messageResourceId)
        val textButton = if (textButtonResourceId == -1) null else getString(textButtonResourceId)
        showNotifyDialog(message, title, textButton)
    }

    open fun showNotifyDialog(message: String, title: String, textButton: String? = null) {
        val notifyDialog = NotifyDialog(this, title, message, textButton)
        notifyDialog.show()
        notifyDialog.window?.setGravity(Gravity.CENTER)
        notifyDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showConfirmDialog(
        titleResourceId: Int,
        messageResourceId: Int = -1,
        positiveTitleResourceId: Int,
        negativeTitleResourceId: Int,
        textButtonResourceId: Int = -1,
        callback: ConfirmDialog.ConfirmCallback
    ) {

        val title = getString(titleResourceId)
        val message = if (messageResourceId != -1) getString(messageResourceId) else null
        val negativeButtonTitle = getString(negativeTitleResourceId)
        val positiveButtonTitle = getString(positiveTitleResourceId)
        val textButton = if (textButtonResourceId == -1) null else getString(textButtonResourceId)
        showConfirmDialog(
            title,
            message,
            negativeButtonTitle,
            positiveButtonTitle,
            textButton,
            callback
        )
    }

    open fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String,
        negativeButtonTitle: String,
        textButton: String?,
        callback: ConfirmDialog.ConfirmCallback
    ) {
        val confirmDialog = ConfirmDialog(
            context = this,
            title = title,
            message = message,
            positiveButtonTitle = positiveButtonTitle,
            negativeButtonTitle = negativeButtonTitle,
            callback = callback
        )
        confirmDialog.show()
        confirmDialog.window?.setGravity(Gravity.CENTER)
        confirmDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showLanguageDialog() {
        val languages = arrayOf("English", "Tiếng Việt")
        val languageCodes = arrayOf("en", "vi")
        val preferenceHelper = PreferenceHelper(this)
        val currentLanguage = preferenceHelper.getLanguage() ?: Locale.getDefault().language

        val dialogTitle = if (currentLanguage == "vi") "Chọn ngôn ngữ" else "Choose language"

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setItems(languages) { _, which ->
                val selectedLanguageCode = languageCodes[which]
                preferenceHelper.saveLanguage(selectedLanguageCode)

                // Apply the new language
                val locale = Locale(selectedLanguageCode)
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                this.resources.updateConfiguration(config, this.resources.displayMetrics)

                // Restart Activity to apply language change
                recreate()
            }
            .show()
    }

    open fun intentToActivity(activity: Class<out Activity>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }





}