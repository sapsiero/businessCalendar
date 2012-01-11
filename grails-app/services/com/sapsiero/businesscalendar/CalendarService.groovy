package com.sapsiero.businesscalendar

class CalendarService {

    boolean isWorkingDay(Date date, String calshortname) {
        def cal = Calendar.findByShortname(calshortname)
        if (cal) 
            isWorkingDay(date, cal)
        else
            throw new BcCalendarNotFoundException("BcCalendar '${calshortname}' not configured.")
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
        validateCalendar(date, cal, days, false)
        def nextdate = date
        (0..days).each() { day ->
            if (day != 0)
                nextdate += (days > 0 ? 1 : -1)
            while(isHoliday(nextdate, cal)) 
                nextdate += (days >= 0 || day == 0 ? 1 : -1)
        }
        nextdate
    }

    Date addWorkingDay(Date date, int days, String calshortname) {
        def cal = Calendar.findByShortname(calshortname)
        if (cal) {
            validateCalendar(date, cal, days, false)
            addWorkingDay(date, days, cal)
        } else
            throw new BcCalendarNotFoundException("BcCalendar '${calshortname}' not configured.")
    }

    Date addHoliday(Date date, int days, Calendar cal) {
        validateCalendar(date, cal, days, true)
        def nextdate = date
        (1..days).each() {
            nextdate++
            while(isWorkingDay(nextdate, cal))
                nextdate++
        }
        nextdate
    }

    Date addHoliday(Date date, int days, String calshortname) {
        def cal = Calendar.findByShortname(calshortname)
        if (cal) {
            validateCalendar(date, cal, days, true)
            addHoliday(date, days, cal)
        } else
            throw new BcCalendarNotFoundException("BcCalendar '${calshortname}' not configured.")
    }

    private boolean validateCalendar(Date date, Calendar cal, int days, boolean holiday) {
        if (!cal.monday && !cal.tuesday && !cal.wednesday && !cal.thursday && !cal.friday && !cal.saturday && !cal.sunday) {
            def list = cal.except.findAll { obj ->
                (date >= 0 ? obj.date > date : obj.date < date) && (holiday ? !obj.additionalDay : obj.additionalDay)
            }
            if (list.size() > Math.abs(days))
                true
            else            
                throw new BcCalendarMisconfiguredException("Calendar '${cal.shortname}' does not contain regular workingdays and irregular ${holiday ? 'holy' : 'working '}days only count to ${list.size()} while more than ${Math.abs(days)} are needed.")
        }
        else
            true
    }

}
