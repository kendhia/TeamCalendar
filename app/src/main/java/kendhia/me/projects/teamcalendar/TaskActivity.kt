package kendhia.me.projects.teamcalendar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kendhia.me.projects.teamcalendar.Fragment.NewTaskFragment
import kendhia.me.projects.teamcalendar.Fragment.TaskDetailsFragment

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        supportActionBar!!.hide()

        val ft = supportFragmentManager.beginTransaction()
        val fragType = intent.getStringExtra(FRAG_TYPE)
        when (fragType) {
            NEW_TASK_FRAG -> {
                ft.replace(R.id.fullscreen_content, NewTaskFragment())
                ft.addToBackStack(null)
                ft.commit()
            }
            TASK_DETAILS_FRAG -> {
                ft.replace(R.id.fullscreen_content, TaskDetailsFragment())
                ft.addToBackStack(null)
                ft.commit()
            }
            else -> {
                ft.replace(R.id.fullscreen_content, NewTaskFragment())
                ft.addToBackStack(null)
                ft.commit()

            }
        }


    }

    companion object {
        val NEW_TASK_FRAG = "newtask"
        val TASK_DETAILS_FRAG = "taskdetails"
        val FRAG_TYPE = "fragtype"
    }
}
