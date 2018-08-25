package kendhia.me.projects.teamcalendar.Model


class TaskEntry(val id : String,val createdBy : String,val date : String, val taskDetails : String, val createdByPhoto : String) {
    constructor() : this("", "", "", "", "")
}