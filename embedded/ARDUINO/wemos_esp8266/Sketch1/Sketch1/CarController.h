#pragma once

#include <Arduino.h>

namespace Car {
	
	class CarController {
		
		
		public:
		
			static inline uint8_t FORWARD       = 0;
			static inline uint8_t BACKWARD      = 1;
			static inline uint8_t FAST_INCREASE = 2;
			static inline uint8_t TURN	        = 3;
			
			void setIO
			(
			uint8_t input1,
			uint8_t input2,
			uint8_t enable,
			uint8_t inputTurn1,
			uint8_t inputTurn2
			);
			
			String process(uint8_t speed, uint8_t options);
			
		private:
		
			uint8_t input1;
			uint8_t input2;
			
			uint8_t enable;
			uint8_t inputTurn1;
			uint8_t inputTurn2;
			
			String go(uint8_t speed, uint8_t options);
			
			
	};
	
}