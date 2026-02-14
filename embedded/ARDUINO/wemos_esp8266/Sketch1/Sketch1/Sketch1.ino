/*
 * LOLIN(WeMos) esp8266 D1 R1 arduino compatible board.
 *
 * Build process:
 * https://docs.arduino.cc/arduino-cli/sketch-build-process/
 *
 * WeMos docs:
 * https://www.wemos.cc/en/latest/tutorials/d32/get_started_with_arduino_d32.html
 * https://arduino-esp8266.readthedocs.io/en/latest/esp8266wifi/readme.html
 *
 * Arduino framework docs:
 * https://docs.arduino.cc/programming/?_gl=1*ives24*_up*MQ..*_ga*NTE2NjY5ODYxLjE3Njk3MDAyMjQ.*_ga_NEXN8H46L5*czE3Njk3MDMzNzAkbzIkZzAkdDE3Njk3MDMzNzAkajYwJGwwJGgxNTA3OTgzMzA1
 *
 */ 

// #include <ESP8266WiFi.h> - For debug logging
#include <IPAddress.h>
#include <ESP8266WiFiAP.h>
#include <ESP8266WebServer.h>

#include "CarController.h"

static ESP8266WiFiAPClass wifi;
static ESP8266WebServer server; // Operates on port 80

uint8_t IN_1 = D9;
uint8_t IN_2 = D10;
uint8_t ENABLE_1 = D8;

Car::CarController car = Car::CarController();

void setupSoftAP();
void logWifiState();

/* 
	REST API

	Motor control:
	http://192.168.4.22/motor?direction=forward&speed=255  
	http://192.168.4.22/motor?direction=backward&speed=255
	http://192.168.4.22/motor?direction=forward&speed=0         
*/

void handleRoot();
void handleMotor();

void setup()
{
	// General lib setup
	
	Serial.begin(115200);
	Serial.println();

	// Access point setup
	
	setupSoftAP();
	
	// Server setup
	
	server.on("/", handleRoot);
	server.on("/motor", handleMotor);
	
	server.begin();
	
	Serial.println("HTTP Server Started");
	
	// IO setup
	
	pinMode(IN_1, OUTPUT);
	pinMode(IN_2, OUTPUT);
	pinMode(ENABLE_1, OUTPUT);
	
	analogWrite(ENABLE_1, 0);
	digitalWrite(IN_1, LOW);
	digitalWrite(IN_2, LOW);
	
	car.setIO(IN_1, IN_2, ENABLE_1);
}

void loop()
{
	server.handleClient();
	
	logWifiState();
}

void setupSoftAP() {
	
	// WeMos soft-AP own IP address
	const IPAddress local_IP(192,168,4,22);

	// IP address if someone wants to talk to external network,
	// however, in soft-AP mode it's just a placeholder,
	// it must be within the same subnet as local_IP
	const IPAddress gateway(192,168,4,9);

	// Subnet mask to differentiate between host and network part
	const IPAddress subnet(255,255,255,0);
	
	const char* SSID = "ESPsoftAP_01";
	
	Serial.print("Setting soft-AP configuration ... ");
	
	bool result = wifi.softAPConfig(local_IP, gateway, subnet);
	Serial.printf("Result = %d\n", result);	
	
	if (!result) {
		Serial.printf("Stopped due to an error !\n");	
		return;
	} 

	Serial.print("Setting SSID configuration ... ");
	result = wifi.softAP(SSID);
	Serial.printf("Result = %d\n", result);
	
	if (!result) {
		Serial.printf("Failed to set soft AP SSID !\n");
	}
	
}

void logWifiState() {
	
	const char* status = "Operating in SOFT_AP_MODE\n";
	String message(status);
	
	IPAddress ip = wifi.softAPIP();
	String wifiNameStr = wifi.softAPSSID();
	uint8_t stationNum = wifi.softAPgetStationNum();
	
	const char* idAddrStr = "AP IP address = ";
	message.concat(idAddrStr);
	message.concat(ip.toString());
	message.concat(", ");
	
	const char* idSSIDStr = "AP SSID = ";
	message.concat(idSSIDStr);
	message.concat(wifiNameStr);
	message.concat(", ");
	
	const char* stationsStr = "Stations connected = ";
	message.concat(stationsStr);
	message.concat(stationNum);
	message.concat(".");
	
	message.concat("\n");
	
	Serial.printf(message.c_str());
	
}

void handleRoot()  {
	server.send(200, "text/html", "<h1>Hello! You are connected to the WeMos D1 R1 !</h1>");
}

void handleMotor() {
	
	const String& uri = server.uri();
	//Serial.printf("Received args = %d, uri = %s", server.args(), uri.c_str());
	
	if (server.args() == 2) {
		
		const String& direction = server.arg("direction");	
		const String& speed = server.arg("speed");
		
		uint8_t options = Car::CarController::FORWARD;
		if (direction.equals("backward")) {
			options = Car::CarController::BACKWARD;
		}
		
		uint8_t speedInt = static_cast<uint8_t>(speed.toInt());
		
		car.process(speedInt, options);	
	}
	
	server.send(200, "text/html", "<h1>Command processed !</h1>");
}
