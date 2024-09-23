package com.example.tep_timeshareexchangeplatform.UI.Fragment.AccountFragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.ActivityCompat.recreate
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.LoginActivity.LoginActivity
import com.example.tep_timeshareexchangeplatform.databinding.FragmentAccountBinding
import com.example.tep_timeshareexchangeplatform.databinding.FragmentHomeBinding
import java.util.Locale

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickEvent()
        logoutDialog()
    }


    private fun setClickEvent() {
        binding.apply {
            // Chỉnh Ngôn ngữ
            llSettingLang.setOnClickListener { (activity as? BaseActivity)?.showLanguageDialog() }
        }

    }

    private fun logoutDialog() {
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }


    }



    private fun openYouTube(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.setPackage("com.google.android.youtube")

        // Kiểm tra xem có ứng dụng YouTube không
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // Nếu không có ứng dụng YouTube, mở bằng trình duyệt
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(webIntent)
        }
    }



}