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
		
		if (options == FORWARD) {
			digitalWrite(input1, HIGH);
			digitalWrite(input2, LOW);
		} else {
			digitalWrite(input1, LOW);
			digitalWrite(input2, HIGH);
		}
		
		analogWrite(enable, speed);
	}

}