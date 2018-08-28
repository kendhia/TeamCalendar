package kendhia.me.projects.teamcalendar.Fragment


import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import kendhia.me.projects.teamcalendar.CustomView.CircleTransformation
import kendhia.me.projects.teamcalendar.FirebaseViewModel
import kendhia.me.projects.teamcalendar.Model.TaskEntry
import kendhia.me.projects.teamcalendar.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TaskDetailsFragment : Fragment() {
    val firebaseViewModel  by lazy {
        ViewModelProviders.of(this).get(FirebaseViewModel::class.java)
    }

    val taskKey by lazy {
        activity!!.intent.getStringExtra(TASK_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewItem =  inflater.inflate(R.layout.fragment_task_details, container, false)
        val progressDialog = ProgressDialog.show(activity, "", "Loading...", true)

        firebaseViewModel.getTaskByKey(taskKey).observe(this, Observer<TaskEntry> { t ->
            if (t != null) {
                viewItem.findViewById<TextView>(R.id.created_by_textView).text = t.createdBy
                viewItem.findViewById<TextView>(R.id.task_date_textView).text = "Date: ${t.date}"
                viewItem.findViewById<TextView>(R.id.task_details_TextView).text = t.taskDetails
                if (t.createdByPhoto.isNotEmpty()) {
                    Picasso.get()
                            .load(t.createdByPhoto)
                            .transform(CircleTransformation())
                            .placeholder(resources.getDrawable(R.drawable.profile))
                            .error(resources.getDrawable(R.drawable.profile))
                            .into(viewItem.findViewById<ImageView>(R.id.created_by_imageView))
                }
                progressDialog.dismiss()
                if (t.done) {
                    viewItem.findViewById<Button>(R.id.mark_done_btn).visibility = View.GONE
                    viewItem.findViewById<ImageView>(R.id.done_imageView).visibility = View.VISIBLE
                } else {
                    viewItem.findViewById<Button>(R.id.mark_done_btn).visibility = View.VISIBLE
                    viewItem.findViewById<ImageView>(R.id.done_imageView).visibility = View.GONE
                }

                viewItem.findViewById<Button>(R.id.mark_done_btn).setOnClickListener {
                    viewItem.findViewById<Button>(R.id.mark_done_btn).visibility = View.GONE
                    viewItem.findViewById<ImageView>(R.id.done_imageView).visibility = View.VISIBLE
                    firebaseViewModel.markTaskDone(taskKey, t)
                }
            }
        })


        return viewItem
    }

    companion object {
        val TASK_KEY = "taskkey"
    }

}
