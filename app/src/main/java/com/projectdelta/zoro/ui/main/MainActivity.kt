package com.projectdelta.zoro.ui.main

import android.os.Bundle
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}