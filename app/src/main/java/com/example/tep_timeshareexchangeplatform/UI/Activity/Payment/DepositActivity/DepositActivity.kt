package com.example.tep_timeshareexchangeplatform.UI.Activity.Payment.DepositActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.ActivityDepositBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositActivity : BaseActivity() {
    private lateinit var binding: ActivityDepositBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.edtMoney.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Không cần xử lý ở đây
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Không cần xử lý ở đây
            }

            override fun afterTextChanged(editable: Editable?) {
                // Loại bỏ TextWatcher tạm thời để tránh loop
                binding.edtMoney.removeTextChangedListener(this)

                val input = editable.toString().replace("[^\\d]".toRegex(), "") // Loại bỏ các ký tự không phải số

                if (input.isNotEmpty()) {
                    // Kiểm tra và loại bỏ số 0 đầu tiên nếu có
                    var cleanedInput = input
                    if (cleanedInput.startsWith("0")) {
                        cleanedInput = cleanedInput.substring(1) // Loại bỏ số 0 đầu tiên
                    }

                    // Định dạng số tiền và thêm ký tự "đ" ở cuối
                    val formatted = formatCurrency(cleanedInput) + " đ"
                    current = formatted
                    binding.edtMoney.setText(formatted)
                    binding.edtMoney.setSelection(formatted.length - 2) // Đặt con trỏ vào vị trí trước "đ"
                }

                // Thêm lại TextWatcher sau khi cập nhật văn bản
                binding.edtMoney.addTextChangedListener(this)
            }

            // Hàm format để chèn dấu chấm vào các số (ví dụ: 100000 -> 100.000)
            private fun formatCurrency(input: String): String {
                return input.reversed().chunked(3).joinToString(".").reversed()
            }
        })


    }
}