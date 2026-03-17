#include "CarController.h"

namespace Car {

	void CarController::setIO
	(
		uint8_t input1, 
		uint8_t input2, 
		uint8_t enable, 
		uint8_t inputTurn1, 
		uint8_t inputTurn2
	) {
		this->input1 = input1;
		this->input2 = input2;
		this->enable = enable;
		this->inputTurn1 = inputTurn1;
		this->inputTurn2 = inputTurn2;
	}
	
	String CarController::process(uint8_t speed, uint8_t options) {
		return go(speed, options);
	}
	
	String CarController::go(uint8_t speed, uint8_t options) {
		
		String info = "INVALID";
		
		// Input 1	 Input 2   Motor    Result
		// High	     Low	   Rotates  Clockwise
		// Low	     High      Rotates  Counter-Clockwise
		// Low	     Low	   Stops    (Coast)
		// High	     High      Stops    (Brake)
		
		if (options & (1<<TURN)) {
			
			info = "TURN mode ";
			
			uint8_t pin1 = inputTurn1;
			uint8_t pin2 = inputTurn2;
			
			if (options & (1<<FORWARD)) {
				digitalWrite(pin1, LOW);
				digitalWrite(pin2, HIGH);
				info += " Right";	
			} else {
				digitalWrite(pin1, HIGH);
				digitalWrite(pin2, LOW);
				info += " Left";
			}
			
			analogWrite(enable, speed);
			
			delay(80);
			
			// Turn Off
			digitalWrite(pin1, LOW);
			digitalWrite(pin2, LOW);
			
			return info;
		}
		
		uint8_t pin1 = input1;
		uint8_t pin2 = input2;
		
		if (options & (1<<FORWARD)) {
			pin1 = input2;
			pin2 = input1;
		}
		
		analogWrite(pin1, 0);
		
		// Do not increase speed gradually
		if (options & (1<<FAST_INCREASE)) {
			info = "Fast Mode";
			analogWrite(pin2, speed);
		} else {
			// Increase speed gradually
			info = "Gradual increase Mode";
			
			analogWrite(pin2, speed);
			delay(100);
			
			// RAMP UP: Gradually increase speed from 0 to speed
			for (int value = 90; value <= speed; value++) {
				analogWrite(pin2, value);
				delay(10);
			}
			
		}
		
		return info;
	}

}