package com.sapsiero.businesscalendar

import grails.plugin.spock.*
import spock.lang.*

class BcCalendarServiceSpec extends UnitSpec {

    def "CalendarService_addWorkingDay"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(BcCalendar)
        new BcCalendar(shortname: "DE", name: "Germany", monday: true, tuesday: true, wednesday: true, thursday: true, friday: true, saturday: false, sunday: false).save()

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

    def "CalendarService_addWorkingDay wrong calendar"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(BcCalendar)
        new BcCalendar(shortname: "DE", name: "Germany", monday: true, tuesday: true, wednesday: true, thursday: true, friday: true, saturday: false, sunday: false).save()

        when: "Test"
        calendarService.addWorkingDay(new Date(), 0, "DEA")

        then: "Test"
        thrown(BcCalendarNotFoundException)
    }

    @Timeout(10)
    def "CalendarService_addWorkingDay wrong calendar config A"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(BcCalendar)
        new BcCalendar(shortname: "DE", name: "Germany").save()

        when: "Test"
        calendarService.addWorkingDay(new Date(), 0, "DE")

        then: "Test"
        def e = thrown(BcCalendarMisconfiguredException)
        e.message == "BcCalendar 'DE' does not contain regular workingdays and irregular working days only count to 0 while more than 0 are needed."
    }

    @Timeout(10)
    def "CalendarService_addWorkingDay wrong calendar config B"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(BcCalendar)
        mockDomain(BcDate)
        def cal = new BcCalendar(shortname: "DE", name: "Germany")
        def date = new BcDate(calendar: cal, date: new Date(111,11,31), additionalDay: true)
        cal.addToExcept(date).save(failOnError: true)
        date.save(failOnError: true)

        when: "Test"
        calendarService.addWorkingDay(new Date(111,11,30), 1, "DE")


        then: "Test"
        BcDate.count() == 1
        BcCalendar calendar = BcCalendar.findByShortname("DE")
        BcDate.findAllByCalendar(calendar).size() == 1
        def e = thrown(BcCalendarMisconfiguredException)
        e.message == "BcCalendar 'DE' does not contain regular workingdays and irregular working days only count to 1 while more than 1 are needed."
    }

    @Timeout(10)
    def "CalendarService_addWorkingDay good calendar config A"() {
        setup:
        def calendarService = new CalendarService()
        mockDomain(BcCalendar)
        mockDomain(BcDate)
        def cal = new BcCalendar(shortname: "DE", name: "Germany")
        def date = new BcDate(calendar: cal, date: new Date(111,11,31), additionalDay: true)
        date.save(failOnError: true)
        cal.addToExcept(date)
        date = new BcDate(calendar: cal, date: new Date(112,0,1), additionalDay: true)
        date.save(failOnError: true)
        cal.addToExcept(date).save(failOnError: true)


        when: "Test"
        def result = calendarService.addWorkingDay(new Date(111,11,30), 1, "DE")


        then: "Test"
        BcDate.count() == 2
        BcCalendar calendar = BcCalendar.findByShortname("DE")
        BcDate.findAllByCalendar(calendar).size() == 2
        date.date == result
    }

}
