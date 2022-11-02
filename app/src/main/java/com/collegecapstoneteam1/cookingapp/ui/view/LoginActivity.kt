package com.collegecapstoneteam1.cookingapp.ui.view

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.collegecapstoneteam1.cookingapp.R
import com.collegecapstoneteam1.cookingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val auth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        binding.lifecycleOwner = this@LoginActivity

        val email = binding.etEmail.text
        val pw = binding.etPassword.text

        with(binding){
            btnLogin.setOnClickListener {
                loginEmail(email.toString(), pw.toString())
            }
            tvSignup.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tvSignup.setOnClickListener {
                regEmail(email.toString(), pw.toString())
            }
            tvBack.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tvBack.setOnClickListener {
                val intent = Intent(this@LoginActivity, StartActivity::class.java)
                startActivity(intent)
                finish()
            }

        }



        setContentView(binding.root)
    }

    fun loginEmail(email: String, pw: String){
        Log.d("@@@@@@@@@@@@", "$email $pw")
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)){
            Toast.makeText(this@LoginActivity, "아이디 및 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else{
            auth?.signInWithEmailAndPassword(email, pw)?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    moveMainPage(task.result?.user)
                } else{
                    //로그인 실패
                    if (pw.length < 6) {
                        Toast.makeText(this@LoginActivity, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "아이디 및 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    fun regEmail(email: String, pw: String){
        Log.d("@@@@@@@@@@@@", "$email $pw")
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)){
            Toast.makeText(this@LoginActivity, "아이디 및 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            auth?.createUserWithEmailAndPassword(email, pw)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 회원가입 완료 -> 바로 로그인
                        moveMainPage(task.result?.user)
                    } else if (task.exception?.message.isNullOrEmpty()) {
                        // 회원가입 실패
                        //Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        Toast.makeText(this@LoginActivity, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 같은 아이디가 있을 시 로그인 시도
                        loginEmail(email, pw)
                    }
                }
        }
    }

    fun moveMainPage(user: FirebaseUser?){
        // 파이어베이스 유저 상태가 있을 경우에만 넘어감
        if(user != null){
            val nextIntent = Intent(this@LoginActivity, MainActivity::class.java)
            nextIntent.putExtra("user", user)
            startActivity(nextIntent)
            finish()
        }
    }
}