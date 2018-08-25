package kendhia.me.projects.teamcalendar

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kendhia.me.projects.teamcalendar.Model.TaskEntry

class FirebaseViewModel(val app : Application) : AndroidViewModel(app) {
    val ref = FirebaseDatabase.getInstance().reference


    fun getTasksByDate( date : String) : LiveData<List<TaskEntry>>{

        val data = MutableLiveData<List<TaskEntry>>()

        ref.orderByChild("date").equalTo(date).addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = ArrayList<TaskEntry>()
                dataSnapshot.children.forEach {
                    items.add(it.getValue(TaskEntry::class.java)!!)
                }
                data.value = items
            }
        })

        return data
    }

}