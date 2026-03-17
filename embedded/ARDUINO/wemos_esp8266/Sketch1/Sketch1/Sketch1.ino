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
 */ 

#include "CarController.h"
#include "NetworkManager.h"

Car::NetworkManager netManager = Car::NetworkManager();
Car::CarController car = Car::CarController();

/* 
	Motor control REST API:

	http://192.168.4.22/motor?options=1&speed=80     // Forward gradually
	http://192.168.4.22/motor?options=2&speed=80     // Backward gradually
	http://192.168.4.22/motor?options=6&speed=255    // Backward fast mode
	http://192.168.4.22/motor?options=5&speed=255    // Forward fast mode  
	http://192.168.4.22/motor?options=4&speed=0      // Stop   
	http://192.168.4.22/motor?options=9&speed=100    // Turn right
	http://192.168.4.22/motor?options=8&speed=100    // Turn left
*/

void handleRoot();
void handleMotor();

void setup()
{
	// IO setup
	
	const uint8_t IN_1 = D8;
	const uint8_t IN_2 = D9;

	const uint8_t ENABLE_1 = D7;
	const uint8_t TURN_IN_1 = D6;
	const uint8_t TURN_IN_2 = D5;

	analogWriteFreq(20000);
	
	pinMode(IN_1, OUTPUT);
	pinMode(IN_2, OUTPUT);
	
	analogWrite(IN_1, 0);
	analogWrite(IN_2, 0);
	
	pinMode(ENABLE_1, OUTPUT);
	pinMode(TURN_IN_1, OUTPUT);
	pinMode(TURN_IN_2, OUTPUT);
	
	digitalWrite(TURN_IN_1, LOW);
	digitalWrite(TURN_IN_2, LOW);
	
	car.setIO(IN_1, IN_2, ENABLE_1, TURN_IN_1, TURN_IN_2);
	
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
