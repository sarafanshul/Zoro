/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projectdelta.zoro.databinding.ActivityExceptionBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.util.NotFound

class ExceptionActivity : BaseViewBindingActivity<ActivityExceptionBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityExceptionBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initUI()

    }

    @SuppressLint("SetTextI18n")
    private fun initUI(){

        binding.emptyView.playAnimation()

        binding.emptyText.text = "Oops! something went Wrong.\n ${NotFound.surpriseMe()}"

    }
}
