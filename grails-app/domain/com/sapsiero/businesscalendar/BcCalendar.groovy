package com.sapsiero.businesscalendar

class BcCalendar {

    String shortname
    String name

    boolean monday
    boolean tuesday
    boolean wednesday
    boolean thursday
    boolean friday
    boolean saturday
    boolean sunday

    static hasMany = [except: BcDate]    

    static constraints = {
        shortname size: 1..32, blank: false, unique: true
        name size: 1..2048, blank: false
        monday()
        tuesday()
        wednesday()
        thursday()
        friday()
        saturday()
        sunday()
        except()
    }

    String toString() {
        return shortname
    }
}
