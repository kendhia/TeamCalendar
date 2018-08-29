package kendhia.me.projects.teamcalendar

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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

    val snackbar by lazy {
        Snackbar.make(findViewById(R.id.mainActivityLayout), getString(R.string.no_tasks), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.add_task) { startNewTaskFrag() }
    }
    lateinit var date : String

    val simpleDateFormat by lazy {
        SimpleDateFormat("dd/M/yyyy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = tasksAdapter
        date = simpleDateFormat.format(Date(calendarView.date))
        updateListOfTasks(date)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${month+1}/$year"
            updateListOfTasks(date)
        }

        newTaskBtn.setOnClickListener {
            startNewTaskFrag()
        }

    }


    private fun startNewTaskFrag() {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.FRAG_TYPE, TaskActivity.NEW_TASK_FRAG)
        intent.putExtra(TaskActivity.NEW_TASK_DATE, simpleDateFormat.parse(date).time)
        startActivity(intent)
    }
    private fun updateListOfTasks(date : String) {
        val progressDialog = ProgressDialog.show(this, "", "Loading...", true)
        tasksAdapter.cleanItems()
        firebaseViewModel.getTasksByDate(date).observe(this, android.arch.lifecycle.Observer {
            if (it != null )  {
                tasksAdapter.addItems(it)
                tasksAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
                if (it.isEmpty()) {
                    snackbar.show()
                } else if (snackbar.isShown) {
                    snackbar.dismiss()
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
