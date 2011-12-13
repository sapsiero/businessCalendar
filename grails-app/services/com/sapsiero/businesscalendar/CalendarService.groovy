package com.sapsiero.businesscalendar

class CalendarService {

    boolean isWorkingDay(Date date, String calshortname) {
        isWorkingDay(date, Calendar.findByShortname(calshortname))
    }

    boolean isWorkingDay(Date date, Calendar cal) {
        def isWorkingDay = false
        
        def c = java.util.Calendar.getInstance()
        c.setTime(date)

        //check standard days
        def day = c.get(java.util.Calendar.DAY_OF_WEEK)

        if (day == java.util.Calendar.SUNDAY && cal.sunday)
            isWorkingDay = true
        else if (day == java.util.Calendar.MONDAY && cal.monday)
            isWorkingDay = true
        else if (day == java.util.Calendar.TUESDAY && cal.tuesday)
            isWorkingDay = true
        else if (day == java.util.Calendar.WEDNESDAY && cal.wednesday)
            isWorkingDay = true
        else if (day == java.util.Calendar.THURSDAY && cal.thursday)
            isWorkingDay = true
        else if (day == java.util.Calendar.FRIDAY && cal.friday)
            isWorkingDay = true
        else if (day == java.util.Calendar.SATURDAY && cal.saturday)
            isWorkingDay = true

        //check exceptions
        if (isWorkingDay) {
        //remove holidays
            if (cal.except.find { obj ->
                obj.date + 1 > date && obj.date <= date && !obj.additionalDay
            })
                isWorkingDay = false
        } else {
        //add working days
            if (cal.except.find { obj ->
                obj.date + 1 > date && obj.date <= date && obj.additionalDay
            })
                isWorkingDay = true
        }
        isWorkingDay
    }

    boolean isHoliday(Date date, String calshortname) {
        !isWorkingDay(date, calshortname)
    }

    boolean isHoliday(Date date, Calendar cal) {
        !isWorkingDay(date, cal)
    }

    Date addWorkingDay(Date date, int days, Calendar cal) {
        def nextdate = date
        while(isHoliday(nextdate, cal)) 
                nextdate++
        (1..days).each() {
            println "$it $nextdate"
            nextdate++
            while(isHoliday(nextdate, cal)) 
                nextdate++
            println "$it $nextdate"
        }
        nextdate
    }

    Date addWorkingDay(Date date, int days, String calshortname) {
        addWorkingDay(date, days, Calendar.findByShortname(calshortname))
    }

    Date addHoliday(Date date, int days, Calendar cal) {
        def nextdate = date
        (1..days).each() {
            nextdate++
            while(isWorkingDay(nextdate, cal))
                nextdate++
        }
        nextdate
    }

    Date addHoliday(Date date, int days, String calshortname) {
        addHoliday(date, days, Calendar.findByShortname(calshortname))
    }

}
