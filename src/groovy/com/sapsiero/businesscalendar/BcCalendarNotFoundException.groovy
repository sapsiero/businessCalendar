package com.sapsiero.businesscalendar

class BcCalendarNotFoundException extends Exception {

    BcCalendarNotFoundException() {
        super()
    }

    BcCalendarNotFoundException(String message) {
        super(message)
    }

    BcCalendarNotFoundException(String message, Throwable cause) {
        super(message, cause)
    }

    BcCalendarNotFoundException(Throwable cause) {
        super(cause)
    }

}
