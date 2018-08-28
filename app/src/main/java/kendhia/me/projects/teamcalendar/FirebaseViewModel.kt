package kendhia.me.projects.teamcalendar

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kendhia.me.projects.teamcalendar.Model.TaskEntry

class FirebaseViewModel(val app : Application) : AndroidViewModel(app) {
    val ref = FirebaseDatabase.getInstance().reference


    fun getTasksByDate( date : String) : LiveData<List<TaskEntry>>{

        val data = MutableLiveData<List<TaskEntry>>()

        ref.orderByChild("date").equalTo(date).addValueEventListener( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("FirebaseViewModel",  "error is ${p0.message}")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("FirebaseViewModel", "date is : $date and its size is ${dataSnapshot.childrenCount}")
                val items = ArrayList<TaskEntry>()
                dataSnapshot.children.forEach {
                    items.add(it.getValue(TaskEntry::class.java)!!)
                }
                data.value = items
            }
        })

        return data
    }

    fun getTaskByKey(key : String) : LiveData<TaskEntry> {

        val dataItem = MutableLiveData<TaskEntry>()
        ref.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                dataItem.value = p0.getValue(TaskEntry::class.java)
            }

        })
        return dataItem
    }

    fun markTaskDone(key: String, taskEntry: TaskEntry) {
        taskEntry.done = true
        ref.child(key).setValue(taskEntry)
    }

}