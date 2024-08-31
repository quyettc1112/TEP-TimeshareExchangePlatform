package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseActivity
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Fragment.AccountFragment.AccountFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.ExchangeFragment.ExchangeFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.HomeFragment.HomeFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.PostingFragment.PostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.TopReosortFragment
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var FragmentAdapter: FragmentAdapter
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpBottomNav()
        observeViewModel()

        viewModel.fetchUserById(1) // Giả sử ID người dùng là 1

    }

    private fun setUpBottomNav(){
        val listFragment: ArrayList<Fragment> = ArrayList()
        listFragment.add(HomeFragment())
        listFragment.add(TopReosortFragment())
        listFragment.add(PostingFragment())
        listFragment.add(ExchangeFragment())
        listFragment.add(AccountFragment())

        FragmentAdapter = FragmentAdapter(this, listFragment)
        binding.vp2Main.adapter = FragmentAdapter
        binding.vp2Main.isUserInputEnabled = false
        binding.vp2Main.offscreenPageLimit = 5

        // Settup change listener
        binding.vp2Main.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.niceBottomNav.setActiveItem(position)
                super.onPageSelected(position)
            }
        })
        binding.niceBottomNav.onItemSelected = {idFragemnt ->
            binding.vp2Main.setCurrentItem(idFragemnt, true)
        }

    }

    private fun observeViewModel() {
        viewModel.user.observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val user = resource.data
                    binding.tvUser.text = user?.name.toString()
                }
                Status.ERROR -> {
                    binding.tvUser.text = resource.message
                }
                Status.LOADING -> {
                    binding.tvUser.text = resource.status.toString()
                }
            }
        })

    }
}