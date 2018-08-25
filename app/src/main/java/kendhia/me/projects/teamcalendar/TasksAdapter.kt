package kendhia.me.projects.teamcalendar

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kendhia.me.projects.teamcalendar.Model.TaskEntry

class TasksAdapter : RecyclerView.Adapter<TasksViewHolder>() {
    val data = ArrayList<TaskEntry>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item_view, parent, false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun addItems(data : List<TaskEntry>) {
        this.data.addAll(data)
    }

}

class TasksViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(taskEntry : TaskEntry) {
        itemView.findViewById<TextView>(R.id.created_by_textView).text = taskEntry.createdBy
        itemView.findViewById<TextView>(R.id.task_details_TextView).text = taskEntry.taskDetails
        Picasso.get()
                .load(taskEntry.createdByPhoto)
                .placeholder(itemView.context.resources.getDrawable(R.drawable.profile))
                .error(itemView.context.resources.getDrawable(R.drawable.profile))
                .into(itemView.findViewById<ImageView>(R.id.created_by_imageView))

        itemView.findViewById<TextView>(R.id.task_details_TextView).setOnClickListener {
            val intent = Intent(itemView.context, TaskActivity::class.java)
            intent.putExtra(TaskActivity.FRAG_TYPE, TaskActivity.TASK_DETAILS_FRAG)
        }
    }
}