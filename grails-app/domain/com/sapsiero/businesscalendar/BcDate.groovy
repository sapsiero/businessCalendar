package com.sapsiero.businesscalendar

class BcDate {

    Date date

    boolean additionalDay

    static belongsTo = [calendar: BcCalendar]

    static constraints = {
        calendar()
        date()
        additionalDay()
    }

    String toString(){
        (additionalDay ? "Working Day: " : "Holiday: ") + date.dateString
    }
}
