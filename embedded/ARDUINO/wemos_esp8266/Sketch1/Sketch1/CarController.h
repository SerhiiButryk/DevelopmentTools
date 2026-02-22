#pragma once

#include <Arduino.h>

namespace Car {
	
	class CarController {
		
		
		public:
		
			static inline uint8_t FORWARD =		  0b0001;
			static inline uint8_t BACKWARD =	  0b0010;
			static inline uint8_t FAST_INCREASE = 0b0100;
			
			void setIO(uint8_t input1, uint8_t input2, uint8_t enable);
			void process(uint8_t speed, uint8_t options);
			
		private:
		
			uint8_t input1;
			uint8_t input2;
			uint8_t enable;
			
			void go(uint8_t speed, uint8_t options);
			
			
	};
	
}