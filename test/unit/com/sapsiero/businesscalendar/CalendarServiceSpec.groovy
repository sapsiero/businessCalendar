package com.sapsiero.businesscalendar

import grails.plugin.spock.*
import spock.lang.*

class CalendarServiceSpec extends UnitSpec {

    def "CalendarService.addWorkingDay()"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(Calendar)
        new Calendar(shortname: "DE", name: "Germany", monday: true, tuesday: true, wednesday: true, thursday: true, friday: true, saturday: false, sunday: false).save()

        expect: "Test"
        targetdate == calendarService.addWorkingDay(sourcedate, days, "DE")

        where:
        targetdate          | sourcedate          | days
        new Date(111,11,16) | new Date(111,11,20) | -2 //Check working day
        new Date(111,11,16) | new Date(111,11,19) | -1 //Check working day
        new Date(111,11,16) | new Date(111,11,18) | -1 //Check weekend day
        new Date(111,11,19) | new Date(111,11,19) | 0 //Check working day
        new Date(111,11,19) | new Date(111,11,18) | 0 //Check weekend day
        new Date(111,11,20) | new Date(111,11,19) | 1 //Check working day
        new Date(111,11,20) | new Date(111,11,18) | 1 //Check weekend day
        new Date(111,11,21) | new Date(111,11,19) | 2 //Check working day
        new Date(111,11,21) | new Date(111,11,18) | 2 //Check weekend day
    }

    def "CalendarService.addWorkingDay() wrong calendar"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(Calendar)
        new Calendar(shortname: "DE", name: "Germany", monday: true, tuesday: true, wednesday: true, thursday: true, friday: true, saturday: false, sunday: false).save()

        when: "Test"
        calendarService.addWorkingDay(new Date(), 0, "DEA")

        then: "Test"
        thrown(BcCalendarNotFoundException)
    }

    @Timeout(10)
    def "CalendarService.addWorkingDay() wrong calendar config"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(Calendar)
        new Calendar(shortname: "DE", name: "Germany").save()

        when: "Test"
        calendarService.addWorkingDay(new Date(), 0, "DE")

        then: "Test"
        thrown(BcCalendarMisconfiguredException)
    }

}
