package com.example.persistence.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.persistence.AppDatabase
import com.example.persistence.Converters
import com.example.persistence.R
import com.example.persistence.dao.PlanningDao
import com.example.persistence.entities.DailyPlanning
import com.example.persistence.fragments.ConfirmFragment
import com.example.persistence.fragments.LoginFragment
import com.example.persistence.fragments.NewPlanningFragment
import com.example.persistence.fragments.SeePlanningFragment
import com.example.persistence.fragments.SignUpFragment
import com.example.persistence.viewModels.PlanningViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlanningActivity : AppCompatActivity(), NewPlanningFragment.NewPlanningInterface{
    private val viewModel: PlanningViewModel by viewModels()
    private lateinit var db : AppDatabase
    private lateinit var planningDao: PlanningDao
    private lateinit var rootView : View
    private lateinit var userId : String
    private val today = Converters.fromLocalDate(date = LocalDate.now())

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.planning_container_layout)
        rootView = findViewById(R.id.planning_container)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java , "TP3" ).build()
        planningDao = db.planningDao()
        userId = intent.getStringExtra("userId").toString()

        viewModel.currentFragment.observe(this) { fragmentTag ->
            val fragment = when (fragmentTag) {
                "see" -> SeePlanningFragment()
                else -> NewPlanningFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.planning_container, fragment)
                .commit()
        }
        if (savedInstanceState == null) {
            viewModel.setCurrentFragment("login") //TODO Modifier le lancement de l'app
        }
        checkPlanning()
    }

    private fun checkPlanning() {
        lifecycleScope.launch {
            val counts = planningDao.checkPlanningExists(userId, today)
            if (counts == 0){
                viewModel.setCurrentFragment("create")
            }
            else{
                retrievePlanning()
            }
        }
    }

    private fun retrievePlanning(){
        lifecycleScope.launch {
            val planning = planningDao.getTodayPlanningForUser(userId, today)
            viewModel.setFirst(planning.slot1Activity.toString())
            viewModel.setSecond(planning.slot2Activity.toString())
            viewModel.setThird(planning.slot3Activity.toString())
            viewModel.setFourth(planning.slot4Activity.toString())
            viewModel.setCurrentFragment("see")
        }
    }

    override suspend fun createPlanning() {
        Log.d("CREATE", "Start")
        val planning = DailyPlanning(
            userId = userId,
            date = today,
            slot1Activity = viewModel.first.value.toString(),
            slot2Activity = viewModel.second.value.toString(),
            slot3Activity = viewModel.third.value.toString(),
            slot4Activity = viewModel.fourth.value.toString())
        planningDao.insertPlanning(planning)
        Log.d("CREATE", "Inserted")
        checkPlanning()
    }
}