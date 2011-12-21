package com.sapsiero.businesscalendar

class BcCalendarMisconfiguredException extends Exception {

    BcCalendarMisconfiguredException() {
        super()
    }

    BcCalendarMisconfiguredException(String message) {
        super(message)
    }

    BcCalendarMisconfiguredException(String message, Throwable cause) {
        super(message, cause)
    }

    BcCalendarMisconfiguredException(Throwable cause) {
        super(cause)
    }

}
