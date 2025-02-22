/**
 *  
 *	Fibaro FGS-222 Double Relay Switch Device Type - For use on Hubitat
 *  
 *	Author: Eric Maycock and Robin Winbourne, updated by Chris Charles
 *	Date: 2018-03-24
 */
 
metadata {
definition (name: "Fibaro Double Relay FGS-222", namespace: "cjcharles0", author: "Eric, Robin and Chris") {
capability "Switch"
capability "Relay Switch"
//capability "Polling"
capability "Configuration"
capability "Refresh"
capability "Zw Multichannel"

attribute "switch1", "string"
attribute "switch2", "string"

command "on1"
command "off1"
command "on2"
command "off2"
    
command "CreateChildren"
command "RemoveChildren"
command "componentOn"
command "componentOff"
command "componentRefresh"
    
command "updateSingleParam" // This custom command can be used with Rule Machine or webCoRE, to send parameter values (paramNr & paramvalue) to the device

fingerprint deviceId: "0x1001", inClusters:"0x86, 0x72, 0x85, 0x60, 0x8E, 0x25, 0x20, 0x70, 0x27"

}
    
   preferences {

	input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true

        input name: "param1", type: "enum", defaultValue: "255", required: true,
            title: "Parameter No. 1 - Activate / deactivate functions ALL ON / ALL OFF. Default value: 255.",
       		options: [
                    ["255" : "255 - ALL ON active, ALL OFF active"],
                    ["0" : "0 - ALL ON is not active ALL OFF is not active"],
                    ["1" : "1 - ALL ON is not active ALL OFF active"],
                    ["2" : "2 - ALL ON active ALL OFF is not active"]
                ]
       
        input name: "param3", type: "enum", defaultValue: "0", required: true,
            title: "Parameter No. 3 - Auto off relay after specified time, with the possibility of manual override - immediate Off after button push. Default value: 0.",
       		options: [
                    ["0" : "0 - Manual override disabled"],
                    ["1" : "1 - Manual override enabled"]
                ]

		input name: "param4", type: "number", range: "0..65535", defaultValue: "0", required: true,
            title: "Parameter No. 4 - Auto off for relay 1.  " +
                   "Available settings:\n" +
                   "[1 - 65535] (0,1 s – 6553,5 s) Time period for auto off, in miliseconds,\n" +
                   "0 - Auto off disabled.\n" +
                   "Default value: 0."

		input name: "param5", type: "number", range: "0..65535", defaultValue: "0", required: true,
            title: "Parameter No. 5 - Auto off for relay 2.  " +
                   "Available settings:\n" +
                   "[1 - 65535] (0,1 s – 6553,5 s) Time period for auto off, in miliseconds,\n" +
                   "0 - Auto off disabled.\n" +
                   "Default value: 0."

		input name: "param6", type: "enum", defaultValue: "0", required: true,
            title: "Parameter No. 6 - Sending commands to control devices assigned to 1st association group (key no. 1). " +
                   "NOTE: Parameter 15 value must be set to 1 to work properly. Default value: 0.",
       		options: [
                    ["0" : "0 - Commands are sent when device is turned on and off"],
                    ["1" : "1 - Commands are sent when device is turned off"],
                	["2" : "2 - Commands are sent when device is turned off"]
                ]

       		input name: "param7", type: "enum", defaultValue: "0", required: true,
            title: "Parameter No. 7 - Sending commands to control devices assigned to 2nd association group (key no. 2). " +
                   "NOTE: Parameter 15 value must be set to 1 to work properly. Default value: 0.",
       		options: [
                    ["0" : "0 - Commands are sent when device is turned on and off"],
                    ["1" : "1 - Commands are sent when device is turned off"],
                	["2" : "2 - Commands are sent when device is turned off"]
                ]

		input name: "param13", type: "enum", defaultValue: "0", required: true,
            title: "Parameter No. 13 - Assigns bistable key status to the device. Default value: 0.",
       		options: [
                    ["0" : "0 - Device changes status on key status change"],
                    ["1" : "1 - Device status depends on key status: ON when the key is ON"]
                ]

		input name: "param14", type: "enum", defaultValue: "1", required: true,
            title: "Parameter No. 14 - Switch type connector, you may choose between momentary and toggle switches. Default value: 1.",
       		options: [
                    ["0" : "0 - Momentary switch"],
                    ["1" : "1 - Toggle switch"]
                ]

		input name: "param15", type: "enum", defaultValue: "0", required: true,
            title: "Parameter No. 15 - Operation of the Dimmer and Roller Shutter Controller - enabling this option allows the user to dim lighting/shut roller by associating Dimmer/Roller Shutter Controller and holding or double press of double switch (only mono-stable switch). Default value: 0.",
       		options: [
                    ["0" : "0 - Dimmer/Roller Shutter Controller control is not active"],
                    ["1" : "1 - Dimmer/Roller Shutter Controller control is active"]
                ]

		input name: "param16", type: "enum", defaultValue: "1", required: true,
            title: "Parameter No. 16 - Saving the state of the device after a power failure. Default value: 1.",
       		options: [
                    ["0" : "0 - Switch returns to 'off' position"],
                    ["1" : "1 - Switch saves its state before power failure"]
                ]

        input name: "param30", type: "enum", defaultValue: "3", required: true,
            title: "Parameter No. 30 - Relay 1 - Response to General Alarm. Default value: 3.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]

     	input name: "param31", type: "enum", defaultValue: "2", required: true,
            title: "Parameter No. 31 - Relay 1 - Response to Flood Alarm. Default value: 2.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]
               
       input name: "param32", type: "enum", defaultValue: "3", required: true,
            title: "Parameter No. 32 - Relay 1 - Response to Smoke, CO, CO2 Alarm. Default value: 3.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]
       
       input name: "param33", type: "enum", defaultValue: "1", required: true,
            title: "Parameter No. 33 - Relay 1 - Response to Temperature Alarm. Default value: 1.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]

		input name: "param39", type: "number", range: "0..65535", defaultValue: "600", required: true,
            title: "Parameter No. 39 - Active flashing alarm time. " +
            	   "This parameter allows to set time parameter used in timed modes.\n" +
                   "Available settings:\n" +
                   "[1-65535][ms].\n" +
                   "Default value: 600."

    	input name: "param40", type: "enum", defaultValue: "3", required: true,
            title: "Parameter No. 40 - Relay 2 - Response to General Alarm. Default value: 3.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]

     	input name: "param41", type: "enum", defaultValue: "2", required: true,
            title: "Parameter No. 41 - Relay 2 - Response to Flood Alarm. Default value: 2.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]
               
       input name: "param42", type: "enum", defaultValue: "3", required: true,
            title: "Parameter No. 42 - Relay 2 - Response to Smoke, CO, CO2 Alarm. Default value: 3.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]
       
       input name: "param43", type: "enum", defaultValue: "1", required: true,
            title: "Parameter No. 43 - Relay 2 - Response to Temperature Alarm. Default value: 1.",
       		options: [
                    ["0" : "0 - Switch does not respond to alarm"],
                    ["1" : "1 - Switch turns on after detecting an alarm"],
                	["2" : "2 - Switch turns off after detecting an alarm"],
                	["3" : "3 - Switch flashes after detecting an alarm"]
                ]
       
    input name: "paramAssociationGroup1", type: "bool", defaultValue: false, required: true,
             title: "The Fibaro Sigle Switch provides the association of three groups.\n\n" +
                    "1st group is assigned to key no. 1.\n" +
                    "Default value: false (true officially)"

        input name: "paramAssociationGroup2", type: "bool", defaultValue: false, required: true,
             title: "2nd group is assigned to key no. 2.\n" +
                    "Default value: false (true officially)"

        input name: "paramAssociationGroup3", type: "bool", defaultValue: false, required: true,
             title: "3rd group reports state of devices. Only one device can be associated to this group.\n" +
                    "Default value: false"
    }
}

def CreateChildren()
{
     try {
        for (i in 1..2) {
	       addChildDevice("hubitat", "Generic Component Switch", "${device.deviceNetworkId}-ep${i}",
		      [completedSetup: true, label: "${device.displayName} (S${i})",
		      isComponent: false, componentName: "ep$i", componentLabel: "Switch $i"])
        }
    } catch (e) {
         log.debug "Didnt create children for some reason: ${e}"
    }
}

def RemoveChildren()
{
	// This will remove all child devices
	log.debug "Removing Child Devices"
	try
	{
		getChildDevices()?.each
		{
			try
			{
				log.debug "Removing ${it.deviceNetworkId} child device"
				deleteChildDevice(it.deviceNetworkId)
			}
			catch (e)
			{
				log.debug "Error deleting ${it.deviceNetworkId}, probably locked into a SmartApp: ${e}"
			}
		}
	}
	catch (err)
	{
		log.debug "Either no child devices exist or there was an error finding child devices: ${err}"
	}
}

def componentOn(child)
{
    if (logEnable) log.debug "componentOn(${child.deviceNetworkId})"
    if (child.deviceNetworkId.substring(child.deviceNetworkId.length()-3) == "ep2") {
        on2()
    }
    else {
        on1()
    }
}

def componentOff(child)
{
    if (logEnable) log.debug "componentOff(${child.deviceNetworkId})"
    if (child.deviceNetworkId.substring(child.deviceNetworkId.length()-3) == "ep2") {
        off2()
    }
    else {
        off1()
    }
}

def componentRefresh(child)
{
    if (logEnable) log.debug "componentRefresh(${child.deviceNetworkId})"
    refresh()
}

def updateChild(String ep, String status)
{
    if (logEnable) log.debug "Updating child with endpoint ${ep} to ${status}"
    if (ep == "both") {
        //Updating both endpoints so do 1 at a time and then exit
        updateChild("1", status)
        updateChild("2", status)
        return
    }
    //First update the parent
    sendEvent(name: "switch"+ep, value: status)
    if (status == "on")
    {
        sendEvent(name: "switch", value: "on")
    }
    else
    {
        //Status is off, so check if the other endpoint is off and update the combined status
        def otherep = ep=="1" ? "switch2" : "switch1"
        if (device.currentState(otherep).getValue() == "off")
        {
            sendEvent(name: "switch", value: "off")
        }
    }

    //Now find and update the child
	def childName = device.deviceNetworkId+"-ep"+ep
	def curdevice = null
	try
	{
		// Got a zone status so first try to find the correct child device
		curdevice = getChildDevices()?.find { it.deviceNetworkId == childName }
	}
	catch (e)
	{
		log.debug "Failed to find child " + childName + " - exception ${e}"
	}

	if (curdevice == null)
	{
		log.debug "Failed to find child called " + childName + " - exception ${e}"
	}
	else
	{
		curdevice?.sendEvent(name: "switch", value: status)
    }
}

def parse(String description)
{
    def result = []
    def cmd = zwave.parse(description)
    if (cmd)
    {
        result += zwaveEvent(cmd)
        //log.debug "Parsed ${cmd} to ${result.inspect()}"
    }
    else
    {
        log.debug "Non-parsed event: ${description}"
    }
    return result
}


def zwaveEvent(hubitat.zwave.commands.basicv1.BasicSet cmd)
{
    log.debug "hubitat.zwave.commands.basicv1.BasicSet ${cmd}"
    def result = []
    result << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format()
    result << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2, commandClass:37, command:2).format()
    response(delayBetween(result, 500)) // returns the result of reponse()
}

def zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd)
{
    if (logEnable) log.debug "hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport ${cmd}"
    def result = []
    result << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format()
    result << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2, commandClass:37, command:2).format()
    response(delayBetween(result, 500)) // returns the result of reponse()
}

def zwaveEvent(hubitat.zwave.commands.multichannelv3.MultiChannelCapabilityReport cmd) 
{
    if (logEnable) log.debug "hubitat.zwave.commands.multichannelv3.MultiChannelCapabilityReport ${cmd}"
    if (cmd.endPoint == 2 ) {
        def currstate = device.currentValue("switch2")
        if (currstate == "on") {
            updateChild("2", "off")
        }
        else if (currstate == "off") {
            updateChild("2", "on")
        }
    }
    else if (cmd.endPoint == 1 ) {
        def currstate = device.currentValue("switch1")
        if (currstate == "on") {
            updateChild("1", "off")
        }
        else if (currstate == "off") {
            updateChild("1", "on")
        }
    }
}

def zwaveEvent(hubitat.zwave.commands.multichannelv4.MultiChannelCapabilityReport cmd) 
{
    if (logEnable) log.debug "hubitat.zwave.commands.multichannelv4.MultiChannelCapabilityReport ${cmd}"
    if (cmd.endPoint == 2 ) {
        def currstate = device.currentState("switch2").getValue()
        if (currstate == "on") {
            updateChild("2", "off")
        }
        else if (currstate == "off") {
            updateChild("2", "on")
        }
    }
    else if (cmd.endPoint == 1 ) {
        def currstate = device.currentState("switch1").getValue()
        if (currstate == "on") {
            updateChild("1", "off")
        }
        else if (currstate == "off") {
            updateChild("1", "on")
        }
    }
}

def zwaveEvent(hubitat.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
    if (logEnable) log.debug "hubitat.zwave.commands.multichannelv3.MultiChannelCmdEncap ${cmd}"
    def map = [ name: "switch$cmd.sourceEndPoint" ]
    def currstate = "off"
       if (cmd.destinationEndPoint == 2 ) {

           if (cmd.parameter.first() > 180) {
            updateChild("2", "on")
           }
         
           else if (cmd.parameter.first() == 0) {
            updateChild("2", "off")
           }
    }
    else if (cmd.destinationEndPoint == 1 ) {

        if (cmd.parameter.first() > 180){
            updateChild("1", "on")
        }
        else if (cmd.parameter.first() == 0) {
            updateChild("1", "off")
        }
    }
}

def zwaveEvent(hubitat.zwave.commands.multichannelv4.MultiChannelCmdEncap cmd) {
    if (logEnable) log.debug "zwave.multichannelv4.MultiChannelCmdEncap ${cmd} - dest:${cmd.destinationEndPoint} src:${cmd.sourceEndPoint} firstparam:${cmd.parameter.first()}"
    if (cmd.sourceEndPoint == 2 ) {
        if (cmd.parameter.first() > 180) {
            updateChild("2", "on")
        }
        else if (cmd.parameter.first() == 0) {
            updateChild("2", "off")
        }
    }
    
    else if (cmd.sourceEndPoint == 1 ) {
        if (cmd.parameter.first() > 180) {
            updateChild("1", "on")
        }
        else if (cmd.parameter.first() == 0) {
            updateChild("1", "off")
        }
    }
    
    else if (cmd.destinationEndPoint == 0 ) {
        if (cmd.parameter.first() > 180) {
            updateChild(cmd.sourceEndPoint.toString(), "on")
           }
        else if (cmd.parameter.first() == 0) {
            updateChild(cmd.sourceEndPoint.toString(), "off")
        }
    }
}

def zwaveEvent(hubitat.zwave.Command cmd) {
    // This will capture any commands not handled by other instances of zwaveEvent
    // and is recommended for development so you can see every command the device sends
    log.debug "catchall ${cmd}"
    return createEvent(descriptionText: "${device.displayName}: ${cmd}")
}

def zwaveEvent(hubitat.zwave.commands.switchallv1.SwitchAllReport cmd) {
    log.debug "hubitat.zwave.commands.switchallv1.SwitchAllReport ${cmd}"
}

def refresh() {
	def cmds = []
	cmds << zwave.manufacturerSpecificV2.manufacturerSpecificGet().format()
	cmds << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format()
	cmds << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2, commandClass:37, command:2).format()
	delayBetween(cmds, 500)
}

def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
	def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
	//log.debug "msr: $msr"
	updateDataValue("MSR", msr)
}

def poll() {
	def cmds = []
	cmds << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format()
    cmds << zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2, commandClass:37, command:2).format()
	delayBetween(cmds, 500)
}

def configure() {
	log.debug "Executing 'configure'"
    def cmds = []

    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param1.toInteger(), parameterNumber:1, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param3.toInteger(), parameterNumber:3, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param4.toInteger(), parameterNumber:4, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param5.toInteger(), parameterNumber:5, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param6.toInteger(), parameterNumber:6, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param7.toInteger(), parameterNumber:7, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param13.toInteger(), parameterNumber:13, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param14.toInteger(), parameterNumber:14, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param15.toInteger(), parameterNumber:15, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param16.toInteger(), parameterNumber:16, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param30.toInteger(), parameterNumber:30, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param31.toInteger(), parameterNumber:31, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param32.toInteger(), parameterNumber:32, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param33.toInteger(), parameterNumber:33, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param39.toInteger(), parameterNumber:39, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param40.toInteger(), parameterNumber:40, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param41.toInteger(), parameterNumber:41, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param42.toInteger(), parameterNumber:42, size: 1))
    cmds << secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: param43.toInteger(), parameterNumber:43, size: 1))
    
    cmds << secureCmd(zwave.associationV2.associationRemove(groupingIdentifier: 1, nodeId: []))
    if (paramAssociationGroup1) {
        cmds << secureCmd(zwave.associationV2.associationSet(groupingIdentifier:1, nodeId:[zwaveHubNodeId])) //0,1,2,3
    }
    cmds << secureCmd(zwave.associationV2.associationRemove(groupingIdentifier: 2, nodeId: []))
    if (paramAssociationGroup2) {
        cmds << secureCmd(zwave.associationV2.associationSet(groupingIdentifier:2, nodeId:[zwaveHubNodeId])) //0,1,2,3
    }
    cmds << secureCmd(zwave.associationV2.associationRemove(groupingIdentifier: 3, nodeId: []))
    if (paramAssociationGroup3) {
        cmds << secureCmd(zwave.associationV2.associationSet(groupingIdentifier:3, nodeId:[zwaveHubNodeId])) //0,1,2,3
    }
    
    return delayBetween(cmds, 500)
}

def updateSingleparam(paramNum, paramValue, paramSize) {
	//log.debug "Updating single Parameter (paramNum: $paramNum, paramValue: $paramValue)"
	secureCmd(zwave.configurationV1.configurationSet(parameterNumber: paramNum, scaledConfigurationValue: paramValue, size: paramSize))
}

/**
* Triggered when Save button is pushed on Preference UI
*/
def updated()
{
	log.debug "Preferences have been changed. Attempting configure()"
	configure()
	//def cmds = configure()
	//response(cmds)
}

def on() {
    log.debug "on"
    delayBetween([
        zwave.switchAllV1.switchAllOn().format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:2).format()
    ], 250)
}
def off() {
    log.debug "off"
    delayBetween([
        zwave.switchAllV1.switchAllOff().format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:2).format()
    ], 250)
}

def on1() {
    log.debug "on1"
    //updateChild("1", "on")
    delayBetween([
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:1, parameter:[255]).format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format()
    ], 250)
}

def off1() {
    log.debug "off1"
    //updateChild("1", "off")
    //zwave.basicV1.basicSet(value: 0x00, destinationEndPoint:1)
    delayBetween([
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:1, parameter:[0]).format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:2).format()
    ], 250)
}

def on2() {
    log.debug "on2"
    //updateChild("2", "on")
    delayBetween([
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:1, parameter:[255]).format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:2).format()
    ], 250)
}

def off2() {
    log.debug "off2"
    //updateChild("2", "off")
    delayBetween([
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:1, parameter:[0]).format(),
        zwave.multiChannelV4.multiChannelCmdEncap(sourceEndPoint:2, destinationEndPoint:2, commandClass:37, command:2).format()
    ], 250)
}


String secureCmd(cmd) {
	if ((getDataValue("zwaveSecurePairingComplete") == "false") || (getDataValue("zwaveSecurePairingComplete") == null)){
		if (logEnable) log.debug "insecure ${cmd}"
		return cmd.format()
	}
	else if (getDataValue("zwaveSecurePairingComplete") == "true" && getDataValue("S2") == null) {
		if (logEnable) log.debug "security-v1 ${cmd}"
		return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
	}
	else {
		if (logEnable) log.debug "secure ${cmd}"
		return secure(cmd)
	}	
}

String secure(String cmd){
    return zwaveSecureEncap(cmd)
}

String secure(hubitat.zwave.Command cmd){
    return zwaveSecureEncap(cmd)
}

def zwaveEvent(hubitat.zwave.commands.supervisionv1.SupervisionGet cmd){
    hubitat.zwave.Command encapCmd = cmd.encapsulatedCommand(commandClassVersions)
    if (encapCmd) {
        zwaveEvent(encapCmd)
    }
    sendHubCommand(new hubitat.device.HubAction(secure(zwave.supervisionV1.supervisionReport(sessionID: cmd.sessionID, reserved: 0, moreStatusUpdates: false, status: 0xFF, duration: 0)), hubitat.device.Protocol.ZWAVE))
}
