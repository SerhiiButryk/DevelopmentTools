#include "CarController.h"

namespace Car {

	void CarController::setIO(uint8_t input1, uint8_t input2, uint8_t enable) {
		this->input1 = input1;
		this->input2 = input2;
		this->enable = enable;
	}
	
	void CarController::process(uint8_t speed, uint8_t options) {
		go(speed, options);
	}
	
	void CarController::go(uint8_t speed, uint8_t options) {
		
		// Input 1	 Input 2   Motor    Result
		// High	     Low	   Rotates  Clockwise
		// Low	     High      Rotates  Counter-Clockwise
		// Low	     Low	   Stops    (Coast)
		// High	     High      Stops    (Brake)
		
		uint8_t pin1 = input1;
		uint8_t pin2 = input2;
		
		if (options & FORWARD == 1) {
			pin1 = input2;
			pin2 = input1;
		}
		
		digitalWrite(pin1, LOW);
		digitalWrite(pin2, HIGH);
		
		// Do not increase speed gradually
		if (options & FAST_INCREASE == 1) {
			analogWrite(enable, speed);
		} else {
			// RAMP UP: Gradually increase speed from 0 to speed
			for (int value = 0; value <= speed; value++) {
				analogWrite(enable, speed);
				delay(20);
			}
		}
	}

}