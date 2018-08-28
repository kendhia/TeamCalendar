package kendhia.me.projects.teamcalendar

import android.content.Intent
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kendhia.me.projects.teamcalendar.CustomView.CircleTransformation
import kendhia.me.projects.teamcalendar.Fragment.TaskDetailsFragment
import kendhia.me.projects.teamcalendar.Model.TaskEntry

class TasksAdapter : RecyclerView.Adapter<TasksViewHolder>() {
    private val data = ArrayList<TaskEntry>()
    private val firebaseRef = FirebaseDatabase.getInstance().reference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item_view, parent, false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(data[position], firebaseRef)
    }

    fun addItems(data : List<TaskEntry>) {
        this.data.addAll(data)
    }

    fun cleanItems() {
        this.data.clear()
    }

}

class TasksViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(taskEntry : TaskEntry, firebaseRef : DatabaseReference) {
        itemView.findViewById<TextView>(R.id.created_by_textView).text = taskEntry.createdBy
        itemView.findViewById<TextView>(R.id.task_details_TextView).text = taskEntry.taskDetails

        if (taskEntry.createdByPhoto.isNotEmpty()) {
            Picasso.get()
                    .load(taskEntry.createdByPhoto)
                    .transform(CircleTransformation())
                    .placeholder(itemView.context.resources.getDrawable(R.drawable.profile))
                    .error(itemView.context.resources.getDrawable(R.drawable.profile))
                    .into(itemView.findViewById<ImageView>(R.id.created_by_imageView))
        }

        itemView.findViewById<TextView>(R.id.task_details_TextView).setOnClickListener {
            val intent = Intent(itemView.context, TaskActivity::class.java)
            intent.putExtra(TaskActivity.FRAG_TYPE, TaskActivity.TASK_DETAILS_FRAG)
            intent.putExtra(TaskDetailsFragment.TASK_KEY, taskEntry.id)
            itemView.context.startActivity(intent)
        }

        if (taskEntry.done) {
            itemView.findViewById<Button>(R.id.mark_done_btn).visibility = View.GONE
            itemView.findViewById<ImageView>(R.id.done_imageView).visibility = View.VISIBLE

        } else {
            itemView.findViewById<Button>(R.id.mark_done_btn).visibility = View.VISIBLE
            itemView.findViewById<ImageView>(R.id.done_imageView).visibility = View.GONE
            itemView.findViewById<Button>(R.id.mark_done_btn).setOnClickListener {
                taskEntry.done = true
                firebaseRef.child(taskEntry.id).setValue(taskEntry)
                it.visibility = View.GONE
                itemView.findViewById<ImageView>(R.id.done_imageView).visibility = View.VISIBLE
            }
        }
    }

}