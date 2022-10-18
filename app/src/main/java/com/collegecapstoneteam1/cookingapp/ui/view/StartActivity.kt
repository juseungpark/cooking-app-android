package com.collegecapstoneteam1.cookingapp.ui.view

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.collegecapstoneteam1.cookingapp.R
import com.collegecapstoneteam1.cookingapp.databinding.ActivityStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class StartActivity : AppCompatActivity() {
    private var _binding: ActivityStartBinding? = null
    private val binding get() = _binding!!


    private val auth by lazy{
        FirebaseAuth.getInstance()
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    // Google 로그인 옵션 구성
    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val googleSignClient by lazy {
        GoogleSignIn.getClient(this@StartActivity, googleSignInOptions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =
            DataBindingUtil.setContentView(this@StartActivity, R.layout.activity_start)
        binding.lifecycleOwner = this@StartActivity


        val email = binding.etEmail.text
        val pw = binding.etPwassword.text

        setResultSignUp()

        with(binding){
            btnLogin.setOnClickListener {
                loginEmail(email.toString(), pw.toString())
            }

            // 구글 로그인
            btnGoogleSignIn.setOnClickListener{
                loginWithGoogle()
            }

            //게스트로 로그인
            tvLoginguest.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tvLoginguest.setOnClickListener {

                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                finish()
            }

            //회원가입
            tvSignup.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tvSignup.setOnClickListener {
                regEmail(email.toString(), pw.toString())
            }
        }
        // 로그인


        setContentView(binding.root)
    }

    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this@StartActivity)
        account?.let {
            Toast.makeText(this@StartActivity, "Logged In", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(this@StartActivity, "Not Yet", Toast.LENGTH_SHORT).show()
    }

    private fun setResultSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // 정상적으로 결과가 받아와진다면 조건문 실행
                if (result.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task)
                }
            }
    }

    //구글 로그인 성공시 호출하는 함수
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()
            val photoUrl = account?.photoUrl.toString()

            Log.d("로그인한 유저의 이메일", email)
            Log.d("로그인한 유저의 성", familyName)
            Log.d("로그인한 유저의 이름", givenName)
            Log.d("로그인한 유저의 전체이름", displayName)
            Log.d("로그인한 유저의 프로필 사진의 주소", photoUrl)

            val nextIntent = Intent(this@StartActivity, MainActivity::class.java)
            nextIntent.putExtra("user", account)
            startActivity(nextIntent)
            finish()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this@StartActivity, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }

    // ------------------------ 구글 관련 start ------------------------------
    private fun loginWithGoogle() {
        val signInIntent: Intent = googleSignClient.getSignInIntent()
        resultLauncher.launch(signInIntent)
    }

    // 로그아웃
    private fun signOut() {
        // Firebase sign out
        auth.signOut()
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this@StartActivity) {
            //updateUI(null)
        }
    }

    // 구글 회원탈퇴
    private fun revokeAccess() {
        // Firebase sign out
        auth.signOut()
        googleSignInClient.revokeAccess().addOnCompleteListener(this@StartActivity) {

        }
    }

    // ------------------------ 구글 관련 end ------------------------------

    // ------------------------ 이메일 관련 start ------------------------------
    fun loginEmail(email: String, pw: String){
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)){
            Toast.makeText(this@StartActivity, "아이디 및 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else{
            auth?.signInWithEmailAndPassword(email, pw)?.addOnCompleteListener{
                    task ->

                if(task.isSuccessful){
                    moveMainPage(task.result?.user)
                } else{
                    //로그인 실패
                    if (pw.length < 6) {
                        Toast.makeText(this@StartActivity, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@StartActivity, "아이디 및 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    fun regEmail(email: String, pw: String){
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)){
            Toast.makeText(this@StartActivity, "아이디 및 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            auth?.createUserWithEmailAndPassword(email, pw)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 회원가입 완료 -> 바로 로그인
                        moveMainPage(task.result?.user)
                    } else if (task.exception?.message.isNullOrEmpty()) {
                        // 회원가입 실패
                        //Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        Toast.makeText(this@StartActivity, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 같은 아이디가 있을 시 로그인 시도
                        loginEmail(email, pw)
                    }
                }
        }
    }
    // ------------------------ 이메일 관련 end ------------------------------

    // 로그인 성공시 다음페이지로 넘어가는 함수
    fun moveMainPage(user:FirebaseUser?){
        // 파이어베이스 유저 상태가 있을 경우에만 넘어감
        if(user != null){
            val nextIntent = Intent(this@StartActivity, MainActivity::class.java)
            nextIntent.putExtra("user", user)
            startActivity(nextIntent)
            finish()
        }
    }

}