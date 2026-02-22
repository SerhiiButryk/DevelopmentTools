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

#include "CarController.h"
#include "NetworkManager.h"

const uint8_t ENABLE_1 = D8;
const uint8_t IN_1 = D9;
const uint8_t IN_2 = D10;

Car::NetworkManager netManager = Car::NetworkManager();
Car::CarController car = Car::CarController();

/* 
	Motor control REST API:

	http://192.168.4.22/motor?options=1&speed=255    // Forward gradually
	http://192.168.4.22/motor?options=2&speed=255    // Backward gradually
	http://192.168.4.22/motor?options=6&speed=200    // Backward fast increase mode
	http://192.168.4.22/motor?options=5&speed=200    // Forward fast increase mode  
	http://192.168.4.22/motor?options=4&speed=0      // Stop     
*/

void handleRoot();
void handleMotor();

void setup()
{
	// IO setup
	
	pinMode(IN_1, OUTPUT);
	pinMode(IN_2, OUTPUT);
	pinMode(ENABLE_1, OUTPUT);
	
	digitalWrite(IN_1, LOW);
	digitalWrite(IN_2, LOW);
	analogWrite(ENABLE_1, 0);
	
	car.setIO(IN_1, IN_2, ENABLE_1);
	
	// General lib setup
	
	Serial.begin(115200);

	// Access point setup
	
	netManager.setupSoftAP();
	
	// Server setup
	
	netManager.setHandler("/", handleRoot);
	netManager.setHandler("/motor", handleMotor);
	
	netManager.startServer();
	
	Serial.println("HTTP server started");
	
}

void loop()
{
	netManager.handleClient();
	netManager.logState();	
}

void handleRoot()  {
	netManager.sendOkResponse("<h1>Hello! You are connected to the WeMos D1 R1 !</h1>");
}

void handleMotor() {
	
	std::array<int, 2> args = netManager.getArgsForMotorControll();
	
	if (args[0] != -1 && args[1] != -1) {
		// Have args
		uint8_t speed = args[0];
		uint8_t options = args[1];
		car.process(speed, options);	
		
		String message = "<h1>Got motor control command.\nSpeed = ";
		message += speed;
		message += ", options = ";
		message += options;
		netManager.sendOkResponse(message.c_str());
		return;
	}
	
	netManager.sendOkResponse("<h1>Got motor control command, but NO args !</h1>");
}
