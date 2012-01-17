package com.sapsiero.businesscalendar

import grails.plugin.spock.*

class BcCalendarSpec extends UnitSpec {

    def "BcCalendar_save"() {
        setup:
        mockDomain(BcCalendar)
        mockDomain(BcDate)

        when: "Test"
        new BcCalendar(shortname: "DE", name: "Germany").save()

        then: "Test"
        BcCalendar.findByShortname("DE") != null
    }

}
