package com.example.persistence.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.persistence.AppDatabase
import com.example.persistence.R
import com.example.persistence.dao.UserDao
import com.example.persistence.fragments.ConfirmFragment
import com.example.persistence.fragments.LoginFragment
import com.example.persistence.fragments.SignUpFragment
import com.example.persistence.viewModels.FirstViewModel
import com.example.persistence.viewModels.LoginViewModel
import com.example.persistence.viewModels.SignUpViewModel
import com.google.android.material.snackbar.Snackbar




class FirstActivity : AppCompatActivity(), LoginFragment.LoginFragmentInterface,SignUpFragment.SignupFragmentInterface, ConfirmFragment.ConfirmSignupInterface {
    private val viewModel: FirstViewModel by viewModels()
    private lateinit var db : AppDatabase
    private lateinit var userDao : UserDao
    private val signUpViewModel : SignUpViewModel by viewModels()
    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var rootView : View



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_layout)
        rootView = findViewById(R.id.login_fragment_container)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java , "TP3" ).fallbackToDestructiveMigration().build()
        userDao = db.userDao()

        viewModel.currentFragment.observe(this) { fragmentTag ->
            val fragment = when (fragmentTag) {
                "login" -> LoginFragment()
                "signup" -> SignUpFragment()
                "confirm" -> ConfirmFragment()
                else -> LoginFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.login_fragment_container, fragment)
                .commit()
        }
        if (savedInstanceState == null) {
            viewModel.setCurrentFragment("login") //TODO Modifier le lancement de l'app
        }
    }
    override fun onSwitchToSignUp() {
        viewModel.setCurrentFragment("signup")
    }

    override suspend fun onLogin() {
        Log.d("LOGIN", "Callback")
        val user = userDao.checkLoginInfos(loginViewModel.login.value.toString(),loginViewModel.password.value.toString())
        Log.d("USER",loginViewModel.login.value.toString())
        if (user==null){
            Log.d("LOGIN", "NO ACCOUNTS FOUND")
            Snackbar.make(rootView, "Identifiant ou Mot de passe incorrect", Snackbar.LENGTH_SHORT).show()
        }
        else{
            Log.d("LOGIN ACCEPTED", "")
            val intent = Intent(this, PlanningActivity::class.java)
            intent.putExtra("userId", user.login)
            startActivity(intent)
        }
    }

    override fun onSwitchToLogin() {
        Log.d("switch", "CallBack")
        viewModel.setCurrentFragment("login")
    }

    override suspend fun signup() {
        Log.d("signup", signUpViewModel.login.value.toString())
        val counts = userDao.checkLoginExists(signUpViewModel.login.value.toString())
        if (counts>0){
            Snackbar.make(rootView, "Un compte est déjà associé à cet identifiant", Snackbar.LENGTH_SHORT).show()
        }
        else{
            viewModel.setCurrentFragment("confirm")
        }
    }

    override suspend fun confirmSignup() {
        Log.d("confirm", "Start of method")
        userDao.insertUser(signUpViewModel.toUser())
        val user = userDao.getUserByLogin(signUpViewModel.login.value.toString())
        Log.d("confirm", user.toString())
        //IMPORTANT : Ne pas oublier de changer le fragment
        viewModel.setCurrentFragment("login")
    }

}