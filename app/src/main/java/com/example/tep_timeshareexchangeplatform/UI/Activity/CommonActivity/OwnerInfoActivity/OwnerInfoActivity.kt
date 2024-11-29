package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.OwnerInfoActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.SentRequestDTO
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.ActivityOwnerInfoBinding
import com.example.tep_timeshareexchangeplatform.databinding.DialogUpdateCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityOwnerInfoBinding
    private val ownerContactInfoViewModel: OwnerContactInfoViewModel by viewModels()
    private lateinit var tokenManager: TokenManager
    private var postingId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOwnerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tokenManager = TokenManager(this)
        getIntentValue()
        eventClickToolbar()
    }

    private fun getIntentValue() {
        postingId = intent.getIntExtra(Constant.OWNER_POSTING_ID, 0)
        if (postingId == 0) {
            finish()
        }

        observePostContactRequest()
        eventClickSentContactRequest()
        bindDataToForm()

    }

    private fun observePostContactRequest() {
        ownerContactInfoViewModel.postContactRequestResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showLoadingWaiting(true)
                }

                Status.SUCCESS -> {
                    hideLoadingWaiting()
                    Toast.makeText(this, "Gửi yêu cầu thành công", Toast.LENGTH_SHORT).show()
                    finish()
                }

                Status.ERROR -> {
                    hideLoadingWaiting()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun bindDataToForm() {
        if (!tokenManager.isLoggedIn()) {
            finish()
        }

        val profileUser = tokenManager.getProfileInfo()
        binding.edtFullname.setText(profileUser?.fullName ?: "")
        binding.edtPhone.setText(profileUser?.phone ?: "")


    }

    private fun eventClickToolbar() {
        binding.customToolbar.onStartIconClick = {
            onBackPressed()
        }
    }

    private fun eventClickSentContactRequest() {
        binding.btnSentContact.setOnClickListener {
            if (validateForm()) {
                val sentRequestDTO = SentRequestDTO(
                    binding.edtFullname.text.toString().trim(),
                    binding.edtPhone.text.toString().trim(),
                    binding.edtNoteMessage.text.toString().trim()
                )


                callSentRequestToOwner(postingId, sentRequestDTO)
            }
        }

    }

    private fun callSentRequestToOwner(postingId: Int, sentRequestDTO: SentRequestDTO) {
        if (!tokenManager.isLoggedIn()) {
            Toast.makeText(
                this,
                "Vui lòng đăng nhập để thực hiện chức năng này",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        ownerContactInfoViewModel.postContactRequest(
            tokenManager.getAccessToken().toString(),
            postingId,
            sentRequestDTO
        )

    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Kiểm tra Tên đầy đủ
        val fullName = binding.edtFullname.text.toString().trim()
        if (fullName.isBlank()) {
            binding.edtFullname.error = "Tên đầy đủ không được để trống"
            isValid = false
        }

        // Kiểm tra Số điện thoại
        val phoneNumber = binding.edtPhone.text.toString().trim()
        if (phoneNumber.isBlank()) {
            binding.edtPhone.error = "Số điện thoại không được để trống"
            isValid = false
        } else if (!phoneNumber.matches(Regex("^[0-9]{10,11}$"))) {
            binding.edtPhone.error = "Số điện thoại không hợp lệ"
            isValid = false
        }

        // Kiểm tra Nội dung
        val noteMessage = binding.edtNoteMessage.text.toString().trim()
        if (noteMessage.isBlank()) {
            binding.edtNoteMessage.error = "Nội dung không được để trống"
            isValid = false
        }

        return isValid
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}