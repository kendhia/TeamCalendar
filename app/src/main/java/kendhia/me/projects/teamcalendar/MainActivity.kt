package kendhia.me.projects.teamcalendar

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val firebaseViewModel  by lazy {
        ViewModelProviders.of(this).get(FirebaseViewModel::class.java)
    }

    val tasksAdapter by lazy {
        TasksAdapter()
    }

    val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    val calendarView by lazy {
        findViewById<CalendarView>(R.id.calendarView)
    }

    val newTaskBtn by lazy {
        findViewById<FloatingActionButton>(R.id.addNewTaskFloatingBtn)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = tasksAdapter
        updateListOfTasks(""
        )
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth/$month/$year"
            updateListOfTasks(date)
            Toast.makeText(application , date, Toast.LENGTH_LONG).show()
        }

        newTaskBtn.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(TaskActivity.FRAG_TYPE, TaskActivity.NEW_TASK_FRAG)
            startActivity(intent)
        }

    }

    private fun updateListOfTasks(date : String) {
        firebaseViewModel.getTasksByDate(date).observe(this, android.arch.lifecycle.Observer {
            if (it != null )  {
                tasksAdapter.addItems(it)
                tasksAdapter.notifyDataSetChanged()
            }
        })
    }

}
