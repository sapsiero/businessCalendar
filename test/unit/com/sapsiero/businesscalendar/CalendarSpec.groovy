package com.sapsiero.businesscalendar

import grails.plugin.spock.*
import spock.lang.*

class CalendarSpec extends UnitSpec {

    def "CalendarService.addWorkingDay() wrong calendar config B"() {
        setup:
        mockDomain(Calendar)
        mockDomain(BcDate)

        when: "Test"
        new Calendar(shortname: "DE", name: "Germany").save()

        then: "Test"
        Calendar.findByShortname("DE") != null
    }

}
