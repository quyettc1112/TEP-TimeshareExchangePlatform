package com.example.tep_timeshareexchangeplatform.UI.Activity.UserActivity.MyPostingActivity.PricingSupportActivity

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityPricingSupportBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogPriceInputBinding

class PricingSupportActivity : BaseActivity() {
    private lateinit var binding: ActivityPricingSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPricingSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnChangePrice.setOnClickListener {
            showPriceSupport()
        }



    }

    private fun showPriceSupport() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_price_input, null)
        val binding = DialogPriceInputBinding.bind(dialogView)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()


        binding.btnAcceptPrice.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}