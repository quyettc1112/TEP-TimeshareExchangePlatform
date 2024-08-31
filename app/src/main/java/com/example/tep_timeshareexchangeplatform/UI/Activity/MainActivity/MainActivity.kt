package com.example.tep_timeshareexchangeplatform.UI.Activity.MainActivity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tep_timeshareexchangeplatform.Common.Adapter.FragmentAdapter
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Fragment.AccountFragment.AccountFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.ExchangeFragment.ExchangeFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.HomeFragment.HomeFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.PostingFragment.PostingFragment
import com.example.tep_timeshareexchangeplatform.UI.Fragment.TopResortFragment.TopReosortFragment
import com.example.tep_timeshareexchangeplatform.databinding.ActivityMainBinding
import java.util.Locale


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var FragmentAdapter: FragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {


        val locale = Locale("vi")
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


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

        setUpBottomNav()
    }

    private fun setUpBottomNav(){
        binding.niceBottomNav.onItemSelected = {idFragemnt ->
            binding.vp2Main.setCurrentItem(idFragemnt, true)
        }

    }
}