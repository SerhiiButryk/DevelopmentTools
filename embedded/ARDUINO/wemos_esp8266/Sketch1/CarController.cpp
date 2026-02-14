#include <CarController.h>

namespace Car {

	void CarController::setIO(uint8_t input1, uint8_t input2, uint8_t enable) {
		this->input1 = input1;
		this->input2 = input2;
		this->enable = enable;
	}
	
	void CarController::start() {
		
		// Input 1	 Input 2   Motor Result
		// High	     Low	   Rotates  Clockwise
		// Low	     High      Rotates  Counter-Clockwise
		// Low	     Low	   Stops    (Coast)
		// High	     High      Stops    (Brake)
		
		digitalWrite(input1, HIGH);
		digitalWrite(input2, LOW);
		
		analogWrite(enable, 167);
		
	}
	
	void CarController::stop() {
		
		// Input 1	 Input 2   Motor Result
		// High	     Low	   Rotates  Clockwise
		// Low	     High      Rotates  Counter-Clockwise
		// Low	     Low	   Stops    (Coast)
		// High	     High      Stops    (Brake)
		
		digitalWrite(input1, HIGH);
		digitalWrite(input2, LOW);
		
		analogWrite(enable, 0);
		
	}

}