package kendhia.me.projects.teamcalendar.Model


class TaskEntry(var id : String,var createdBy : String,var date : String, var taskDetails : String, var createdByPhoto : String, var done : Boolean) {
    constructor() : this("", "", "", "", "", false)
}