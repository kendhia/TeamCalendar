package kendhia.me.projects.teamcalendar.Fragment


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kendhia.me.projects.teamcalendar.MainActivity
import kendhia.me.projects.teamcalendar.Model.TaskEntry

import kendhia.me.projects.teamcalendar.R
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kendhia.me.projects.teamcalendar.LoginActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NewTaskFragment : Fragment() {
    lateinit var calendarView: CalendarView
    lateinit var taskDetailsInput: TextInputEditText
    lateinit var submitFloatBtn: FloatingActionButton

    private val firebaseRef by lazy {
        FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_new_task, container, false)
        calendarView = itemView.findViewById(R.id.calendarView)
        taskDetailsInput = itemView.findViewById(R.id.text_details_textInput)
        submitFloatBtn = itemView.findViewById(R.id.submit_task_FloatBtn)
        val taskEntry = TaskEntry()

        val account = GoogleSignIn.getLastSignedInAccount(activity)

        if (account == null) {
            startActivity(Intent(activity, LoginActivity::class.java))
        } else {
            if (account.photoUrl != null) {
                taskEntry.createdByPhoto = account.photoUrl.toString()
            }
            taskEntry.createdBy = account.displayName!!
            val sfd = SimpleDateFormat("dd/MM/yyyy")
            taskEntry.date = sfd.format(Date(calendarView.date))

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth -> taskEntry.date = "$dayOfMonth/$month*$year" }

            submitFloatBtn.setOnClickListener {
                taskEntry.taskDetails = taskDetailsInput.text.toString()
                submitTask(taskEntry)
            }
        }

        return itemView
    }


    fun submitTask(task: TaskEntry) {
        val id = firebaseRef.push().key
        if (id == null) {
            task.id = task.date
        } else {
            task.id = id
        }
        firebaseRef.child(task.id).setValue(task)

        Toast.makeText(activity, resources.getString(R.string.task_added), Toast.LENGTH_LONG).show()
        startActivity(Intent(activity, MainActivity::class.java))
    }

}
