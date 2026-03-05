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


const uint8_t IN_1 = D8;
const uint8_t IN_2 = D9;

const uint8_t ENABLE_1 = D10;
const uint8_t L_IN_1 = D11;
const uint8_t R_IN_2 = D12;

Car::NetworkManager netManager = Car::NetworkManager();
Car::CarController car = Car::CarController();

/* 
	Motor control REST API:

	http://192.168.4.22/motor?options=1&speed=80     // Forward gradually
	http://192.168.4.22/motor?options=2&speed=80     // Backward gradually
	http://192.168.4.22/motor?options=6&speed=255    // Backward fast mode
	http://192.168.4.22/motor?options=5&speed=255    // Forward fast mode  
	http://192.168.4.22/motor?options=4&speed=0      // Stop   
	http://192.168.4.22/motor?options=9&speed=255    // Turn right
	http://192.168.4.22/motor?options=8&speed=255    // Turn left
*/

void handleRoot();
void handleMotor();

void setup()
{
	// IO setup
	
	analogWriteFreq(20000);
	
	pinMode(IN_1, OUTPUT);
	pinMode(IN_2, OUTPUT);
	
	analogWrite(IN_1, 0);
	analogWrite(IN_2, 0);
	
	pinMode(ENABLE_1, OUTPUT);
	pinMode(L_IN_1, OUTPUT);
	pinMode(R_IN_2, OUTPUT);
	
	digitalWrite(L_IN_1, LOW);
	digitalWrite(R_IN_2, LOW);
	
	car.setIO(IN_1, IN_2, ENABLE_1, L_IN_1, R_IN_2);
	
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
		String info = car.process(speed, options);	
		
		String message = "<h1>Got motor control command.</h1> <p>Speed = ";
		message += speed;
		message += ", options = ";
		message += options;
		message += " ";
		message += info;
		message += "</p>";
		netManager.sendOkResponse(message.c_str());
		return;
	}
	
	netManager.sendOkResponse("<h1>Got motor control command, but NO args !</h1>");
}
