package com.collegecapstoneteam1.cookingapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.collegecapstoneteam1.cookingapp.R
import com.collegecapstoneteam1.cookingapp.databinding.FragmentSettingBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    //구글로그인
    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val googleSignClient by lazy {
        activity?.let { GoogleSignIn.getClient(it, googleSignInOptions) }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //로그인으로 이동
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)

        auth = FirebaseAuth.getInstance()


        val emailId = (activity as MainActivity).emailId
        Log.d("셋팅프래그먼트", "$emailId")

        if (auth.currentUser?.email != null){
            binding.signInSignUp.text = "로그아웃"
            binding.loginStatus.text = auth.currentUser?.email
            binding.revokeAccess.setOnClickListener {
                revokeAccess()
            }
        } else if (auth.currentUser?.email == null) {
        }

        if (emailId != null) {
            binding.signInSignUp.text = "로그아웃"
            binding.loginStatus.text = emailId
        }

        binding.signInSignUp.setOnClickListener {
           fireLogin()
        }




        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun fireLogin() {
        if (binding.signInSignUp.text == "로그인 / 회원가입"){
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }else if (binding.signInSignUp.text == "로그아웃"){
            binding.signInSignUp.text = "로그인 / 회원가입"
            binding.loginStatus.text = "미로그인"
            auth.signOut()
            activity?.let {
                googleSignClient?.signOut()?.addOnCompleteListener(it) {
                    Toast.makeText(activity, "로그아웃", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun revokeAccess() {
        auth.currentUser?.delete()
        Toast.makeText(activity, "회원탈퇴", Toast.LENGTH_SHORT).show()
    }


}