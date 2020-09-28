/**
 *  Copyright 2017 Chris Charles
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Neo Hub Bridge (Parent Device of Neo Thermostat)
 *
 *  Author: Chris Charles (cjcharles0)
 *  Date: 2017-04-26
 */

import groovy.json.JsonSlurper

metadata {
    definition(name: "Neo Hub Bridge", namespace: "cjcharles0", author: "Chris Charles") {
        capability "Refresh"
        capability "Configuration"
        capability "Polling"

        command "refreshipaddresses"
        command "getthermostats"
        command "removethermostats"
        command "setAllAwayOn"
        command "setAllAwayOff"

        command "childGetDebugState"

        command "childRequestingRefresh"
        command "childAwayOn"
        command "childAwayOff"
        command "childHeat"
        command "childSetTemp"
        command "childHold"
        command "childCancelHold"
        command "childBoostOn"
        command "childBoostOff"
        command "childFrostOn"
        command "childFrostOff"
        command "childSetFrost"
        command "childTimerOn"
        command "childTimerOff"
        command "childTimerHoldOn"
        command "childTimerHoldOff"
        command "childHolidayOn"
        command "childCancelHoliday"
        command "increaseHours"
        command "decreaseHours"
        command "increaseDays"
        command "decreaseDays"
        command "refreshHolidayButtonText"
        command "holidayOnOrCancel"

        command "testcommand"

    }

    simulator {}

    preferences {
        input("password", "password", title: "Password", required: false, displayDuringSetup: false)
        input("neohubip", "string", title: "NeoHub IP Address", description: "e.g. 192.168.1.11", required: true, displayDuringSetup: true)
        input("prestatname", "string", title: "Add before stat name", description: "e.g. 'Thermostat' would give 'Thermostat Kitchen'", required: false, displayDuringSetup: true)
        input("poststatname", "string", title: "Add after stat name", description: "e.g. 'Thermostat' would give 'Kitchen Thermostat'", required: false, displayDuringSetup: true)
        input("enable_debug", "bool", title: "Enable debug mode printing?", description: "Adds a few extra debug lines", required: false, displayDuringSetup: true)
    }

    tiles(scale: 2) {

        valueTile("lastcommand", "lastcommand", decoration: "flat", width: 6, height: 2) {
            state "lastcommand",
                label: '${currentValue}',
                icon: "st.Home.home2"
        }

        valueTile("stip", "stip", decoration: "flat", width: 2, height: 1) {
            state "stip",
                label: 'ST IP Addr:\r\n${currentValue}'
        }
        valueTile("neoip", "neoip", decoration: "flat", width: 2, height: 1) {
            state "neoip",
                label: 'Neohub IP Addr:\r\n${currentValue}'
        }

        standardTile("refreships", "device.refreships", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
            state "default",
                label: "Refresh IPs",
                action: "refreshipaddresses",
                icon: "st.secondary.refresh"
        }
        standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
            state "configure",
                label: 'Bridge',
                action: "configure",
                icon: "st.secondary.configure"
        }
        standardTile("getthermostats", "device.getthermostats", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
            state "getthermostats",
                label: 'Create Thermostat Devices',
                action: "getthermostats",
                icon: "st.unknown.zwave.remote-controller"
        }
        standardTile("removethermostats", "device.removethermostats", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
            state "removethermostats",
                label: 'Remove Thermostat Devices',
                action: "removethermostats",
                icon: "st.samsung.da.washer_ic_cancel"
        }
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
            state "default",
                label: "Refresh Thermostat Info",
                action: "refresh",
                icon: "st.secondary.refresh"
        }
        standardTile("allaway", "device.allaway", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
            state "off",
                label: 'Set all Thermostats to Away',
                action: "setAllAwayOn"
            state "on",
                label: 'Thermostats Away, Press to cancel',
                action: "setAllAwayOff"
        }

        standardTile("increaseHours", "device.increaseHours", width: 1, height: 1, decoration: "flat") {
            state "default",
                action: "increaseHours",
                label: '+1h\r\nHoliday'
        }
        standardTile("decreaseHours", "device.decreaseHours", width: 1, height: 1, decoration: "flat") {
            state "default",
                action: "decreaseHours",
                label: '-1h\r\nHoliday'
        }
        standardTile("increaseDays", "device.increaseDays", width: 1, height: 1, decoration: "flat") {
            state "default",
                action: "increaseDays",
                label: '+1d\r\nHoliday'
        }
        standardTile("decreaseDays", "device.decreaseDays", width: 1, height: 1, decoration: "flat") {
            state "default",
                action: "decreaseDays",
                label: '-1d\r\nHoliday'
        }
        standardTile("holidayOnOrCancel", "device.holidayOnOrCancel", width: 2, height: 1, decoration: "flat") {
            state "default",
                action: "holidayOnOrCancel",
                label: '${currentValue}'
        }
    }

    main(["lastcommand"])
    details(["lastcommand", "stip", "neoip", "refreships", "configure", "getthermostats", "removethermostats", "holidayOnOrCancel", "decreaseHours", "increaseHours", "decreaseDays", "increaseDays", "allaway", "refresh"])
}

def refresh() {
    log.debug "Refreshing all children (done from Bridge)"
    for (curdevice in getChildDevices()) {
        log.debug "Requesting updated temperature information for ${curdevice}"
        curdevice.refresh()
    }
}
def installed() {
    log.debug "installed()"
    updated()
}
def uninstalled() {
    log.debug "uninstalled()"
    removethermostats()
}
def updated() {
    log.debug "updated()"
    refreshipaddresses()
    state.holidaydays = 0
    state.holidayhours = 0
    if (enable_debug) {
        state.debug = true
        log.debug "Debug print information enabled"
    } else {
        state.debug = false
        log.debug "Debug print information disabled"
    }
}
def ping() {
    //log.debug "ping()"
    //refresh()
}
def poll() {
    //log.debug "poll()"
    //refresh()
}

//These commands will either add or remove the child thermostats, surprisingly....
private getthermostats() {
    //Before sending request for thermostat list we should remove current thermostats
    def cmds = []
    removethermostats()
    log.debug "Requesting List of Thermostats"
    state.lastmessage = "Get zones"
    cmds << getAction("{\"GET_ZONES\":0}")
    return cmds
}
private removethermostats() {
    log.debug "Removing Child Thermostats"
    try {
        getChildDevices()?.each {
            try {
                deleteChildDevice(it.deviceNetworkId)
            } catch (e) {
                log.debug "Error deleting ${it.deviceNetworkId}, probably locked into a SmartApp: ${e}"
            }
        }
    } catch (err) {
        log.debug "Either no children exist or error finding child devices for some reason: ${err}"
    }
}

//These commands might be useful for doing bulk changes on all thermostats
def setAllAwayOn() {
    log.debug "Setting all Thermostats to Away"
    sendEvent(name: "allaway", value: "on", isStateChange: true)
    getAction("{\"AWAY_ON\":0}")
}
def setAllAwayOff() {
    //def cmds = []
    log.debug "Setting all Thermostats to Home"
    sendEvent(name: "allaway", value: "off", isStateChange: true)
    getAction("{\"AWAY_OFF\":0}")
}

//This function helps control debug printing
def childGetDebugState() {
    if (state.debug == true) return true
    else return false
}

//These functions can be called by the children in order to request something from the bridge
def childRequestingRefresh(String dni) {
    //Send Refresh command - this will occur for all thermostats, not just the one which requested it
    state.lastmessage = "Refresh zone"
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    if (state.debug) log.debug "Requesting refreshed info for ${dni}"
    getAction("{\"INFO\":\"${deviceid}\"}")
}
def childAwayOn(String dni) {
    //Do not use, use FrontOn instead
    //Send Child Away on command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting away mode on for child ${deviceid}"
    getAction("{\"AWAY_ON\":\"${deviceid}\"}")
}
def childAwayOff(String dni) {
    //Do not use, use FrontOff instead
    //Send Child Away off command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting away mode off for child ${deviceid}"
    getAction("{\"AWAY_OFF\":\"${deviceid}\"}")
}
def childHeat(String dni) {
    //Send Child Heat mode
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting heat mode for child ${deviceid}"
    getAction("{\"HEAT\":\"${deviceid}\"}")
}
def childSetTemp(String temp, String dni) {
    //Send Child Set Temp command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting ${temp} degrees for child ${deviceid}"
    getAction("{\"SET_TEMP\":[${temp},\"${deviceid}\"]}")
}
def childCancelHold(String dni) {
    //Send Child Set Temp command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting revert to schedule for child ${deviceid}"
    getAction("{\"TIMER_HOLD_OFF\":[0,\"${deviceid}\"]}")
}
def childHold(String temp, String hours, String minutes, String dni) {
    //Send Child Hold command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting Hold at ${temp} degrees for ${hours}h:${mins}m for child ${deviceid}"
    getAction("{\"HOLD\":[{\"temp\":${temp},\"id\":\"\",\"hours\":${hours},\"minutes\":${minutes}},\"${deviceid}\"]}")
}
def childBoostOn(String hours, String minutes, String dni) {
    //Send Child Boost On command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting boost on for child ${deviceid}"
    getAction("{\"BOOST_ON\":[{\"hours\":${hours},\"minutes\":${minutes}},\"${deviceid}\"]}")
}
def childBoostOff(String hours, String minutes, String dni) {
    //Send Child Boost On command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting boost off for child ${deviceid}"
    getAction("{\"BOOST_OFF\":[{\"hours\":${hours},\"minutes\":${minutes}},\"${deviceid}\"]}")
}
def childFrostOn(String dni) {
    //Send Child Frost On command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting frost on for child ${deviceid}"
    getAction("{\"FROST_ON\":\"${deviceid}\"}")
}
def childFrostOff(String dni) {
    //Send Child Frost On command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting frost off for child ${deviceid}"
    getAction("{\"FROST_OFF\":\"${deviceid}\"}")
}
def childSetFrost(String temp, String dni) {
    //Send Child Set Frost command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting set frost at ${temp} degrees for child ${deviceid}"
    getAction("{\"SET_FROST\":[\"${temp}\",\"${deviceid}\"]}")
}
def childTimerOn(String dni) {
    //Send Child Timer On command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting timer on for child ${deviceid}"
    getAction("{\"TIMER_ON\":\"${deviceid}\"}")
}
def childTimerOff(String dni) {
    //Send Child Timer Off command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting timer off for child ${deviceid}"
    getAction("{\"TIMER_OFF\":\"${deviceid}\"}")
}
def childTimerHoldOn(String minutes, String dni) {
    //Send Child Timer Hold On command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting timer hold on for child ${deviceid}"
    getAction("{\"TIMER_HOLD_ON\":[${minutes},\"${deviceid}\"]}")
}
def childTimerHoldOff(String minutes, String dni) {
    //Send Child Timer Hold Off command
    def deviceid = dni.replaceAll("neostat", "").replaceAll("-", " ")
    log.debug "Requesting timer hold off for child ${deviceid}"
    getAction("{\"TIMER_HOLD_OFF\":[${minutes},\"${deviceid}\"]}")
}
def childHolidayOn(String hhmmssddmmyyy, String dni) {
    def dateTime = new Date()
    def currenttimestamp = dateTime.format("HHmmssddMMyyyy", location.timeZone)
    log.debug "Setting Holiday mode from ${currenttimestamp} to ${neohubtimestamp}"
    getAction("{\"HOLIDAY\":[\"${currenttimestamp}\",\"${hhmmssddmmyyy}\"]}")
}
def childCancelHoliday(String dni) {
    //Send request to cancel holiday
    log.debug "Cancel holiday mode"
    getAction("{\"CANCEL_HOLIDAY\":0}")
}

// This function receives the response from the NeoHub bridge and updates things, or passes the response to an individual thermostat
def parse(response) {
    def msg = new String(unhexify(response))
    log.debug msg

    def map = [: ]
    def events = []
    def cmds = []

    if (description == "updated") return
    //def descMap = parseDescriptionAsMap(msg)
    //def body = new String(descMap["body"]())

    def slurper = new JsonSlurper()
    
    try {
        def result = slurper.parseText(msg)
    }
    catch (e) {
        log.debug "Couldnt process, probably partial packet: ${e}"
    }
    
    //def result = slurper.parseText(msg)
    if (state.debug) log.debug result
    if (state.debug) log.debug state.lastmessage
    //device.updateDataValue("lastmessage",val)
    //device.getDataValue("lastmessage"
    
    
    cmds << sendEvent(name: "lastcommand", value: "${result}", isStateChange: true)

    if (state.lastmessage == "Get zones") {
        //Iterate through each of the items in the result to create a device
        for (item in result) {
            def thisthermostatname = ""
            def thisthermostatdni = ""
            //Prepare Thermostat name - prepend and postpend of settings if they exist
            if (prestatname != null) {
                thisthermostatname = thisthermostatname + prestatname + " "
            }
            thisthermostatname = thisthermostatname + item.key
            if (poststatname != null) {
                thisthermostatname = thisthermostatname + " " + poststatname
            }
            //Prepare DNI - Remove spaces and replace with a hyphen to prevent problems with HTML requests
            thisthermostatdni = "neostat${item.key.replaceAll(" ", " - ")}"
            log.debug "Adding child Name: ${thisthermostatname}, DNI: ${thisthermostatdni}, Stat ID: ${item.value} to Hub: ${device.hub.id}"
            try {

                addChildDevice("cjcharles0", "Neo Thermostat", thisthermostatdni, [name: thisthermostatname, isComponent: false])
            } catch (e) {
                log.debug "Couldnt add device, probably already exists: ${e}"
            }
        }
        //Finally send a refresh command to get the latest info for each thermostat
        state.lastmessage = ""
        refresh()
    }

    if (state.lastmessage == "Refresh zone") {
        //We got a command/response for an individual thermostat so send data to thermostat
        if (state.debug) log.debug "Received a response from NeoHub for ${result.devices.device}"
        //Now we try to find the child, and if found then send it the payload
        try {
            def resultdevice = getChildDevices().find {
                it.deviceNetworkId == result.devices.device
            }
            resultdevice?.processNeoResponse(result.devices)
        } catch (e) {
            log.debug "Couldnt process response, probably this child doesnt exist: ${e}"
        }
        state.lastmessage = ""
    }
    return cmds
}

def increaseHours() {
    if (state.holidayhours < 23) {
        state.holidayhours = state.holidayhours + 1
    } else {
        state.holidayhours = 0
        state.holidaydays = state.holidaydays + 1
    }
    refreshHolidayButtonText()
}
def decreaseHours() {
    if (state.holidayhours > 0) {
        state.holidayhours = state.holidayhours - 1
    } else if (state.holidaydays > 0) {
        state.holidayhours = 23
        state.holidaydays = state.holidaydays - 1
    }
    refreshHolidayButtonText()
}
def increaseDays() {
    if (state.holidaydays >= 0) {
        state.holidaydays = state.holidaydays + 1
    } else {
        state.holidaydays = 0
    }
    refreshHolidayButtonText()
}
def decreaseDays() {
    if (state.holidaydays >= 1) {
        state.holidaydays = state.holidaydays - 1
    } else {
        state.holidaydays = 0
        state.holidayhours = 0
    }
    refreshHolidayButtonText()
}
def refreshHolidayButtonText() {
    def cmds = []
    if (state.holidaydays == null) {
        state.holidaydays = 0
    }
    if (state.holidayhours == null) {
        state.holidayhours = 0
    }
    def dateTime = new Date()
    def timestamp = new GregorianCalendar(dateTime.year, dateTime.month, dateTime.date + state.holidaydays, dateTime.hours + state.holidayhours, dateTime.minutes, 0, 0).time
    def finishtimestamp = timestamp.format("HH:mm dd-MMM", location.timeZone) //formatted like this for neohub HHmmssddMMyyyy
    cmds << sendEvent(name: "holidayOnOrCancel", value: "Press to set Holiday\r\n until ${finishtimestamp}", isStateChange: true)
    return cmds
}
def holidayOnOrCancel() {
    //Any function which makes a getAction request cannot return a set of tile update sendEvent's through a cmd style array
    if (state.holidaydays == null) {
        state.holidaydays = 0
    }
    if (state.holidayhours == null) {
        state.holidayhours = 0
    }
    def dateTime = new Date()
    def currenttimestamp = dateTime.format("HHmmssddMMyyyy", location.timeZone)
    def timestamp = new GregorianCalendar(dateTime.year + 1900, dateTime.month, dateTime.date + state.holidaydays, dateTime.hours + state.holidayhours, dateTime.minutes, 0, 0).time
    def uitimestamp = timestamp.format("HH:mm dd-MMM", location.timeZone)
    def neohubtimestamp = timestamp.format("HHmmssddMMyyyy", location.timeZone)

    if (device.currentValue("holidayOnOrCancel").substring(0, 5) == "Press") {
        //Trying to set holiday timer so lets do it
        sendEvent(name: "holidayOnOrCancel", value: "Holiday until ${uitimestamp}\r\nPress to cancel", isStateChange: true)
        childHolidayOn(neohubtimestamp, "hubcommand")
    } else {
        //Likely trying to cancel holiday timer so remove holidays
        refreshHolidayButtonText()
        childCancelHoliday("hubcommand")
    }
}

//These functions are helper functions to talk to the NeoHub Bridge
def getAction(uri) {
    log.debug "uri ${uri}"

    interfaces.rawSocket.connect(neohubip, 4242) //eol: "\r"
    interfaces.rawSocket.sendMessage(uri + "\0\r")
}

private parseDescriptionAsMap(description) {
    description.split(",").inject([: ]) {
        map,param -> def nameAndValue = param.split(":")
        map += [(nameAndValue[0].trim()): nameAndValue[1].trim()]
    }
}

private String unhexify(String hexStr) {
    StringBuilder output = new StringBuilder("");

    for (int i = 0; i < hexStr.length(); i += 2) {
        String str = hexStr.substring(i, i + 2);
        output.append((char) Integer.parseInt(str, 16));
    }

    return output.toString();
}																													

private socketStatus(message) {
    log.debug message
}
