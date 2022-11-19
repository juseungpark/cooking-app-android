package com.collegecapstoneteam1.cookingapp.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.collegecapstoneteam1.cookingapp.R
import com.collegecapstoneteam1.cookingapp.databinding.FragmentSettingBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //로그인으로 이동
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.signInSignUp.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}