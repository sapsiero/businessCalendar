package com.sapsiero.businesscalendar

class BcDate {

    Date date

    boolean additionalDay

    static belongsTo = [calendar: com.sapsiero.businesscalendar.Calendar]

    static constraints = {
        calendar()
        date()
        additionalDay()
    }

    String toString(){
        (additionalDay ? "Working Day: " : "Holiday: ") + date.dateString
    }
}
