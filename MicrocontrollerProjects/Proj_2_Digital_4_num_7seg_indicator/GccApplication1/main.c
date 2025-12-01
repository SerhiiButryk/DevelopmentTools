// 1 MHz frequency for atmega8a
#define F_CPU 1000000UL

#include <avr/io.h>
#include <util/delay.h>

static int digit = 0; 

void selectDigit();

int main()
{
	// Set all PB pins for output, will control a, b, c ... dp segments
	DDRB = 0b11111111;
	// Set 4 PC pins for output, will control d1, d2 ... d4
	DDRC = 0b00001111;
	
	uint8_t digits[] = {
		// a,b,c,d,e,f,g,dp
		0b00000011, // 0
		0b10011111, // 1
		0b00100101, // 2
		0b00001101, // 3
		0b10011001, // 4
		0b01001001, // 5
		0b01000001, // 6
		0b00011111, // 7
		0b00000001, // 0
		0b00001001  // 9
	};
	
	// 5th Bit is set to use button input
	// and to select digit
	uint8_t ports[] = {
		0b00010001, // port 1
		0b00010010, // port 2
		0b00010100, // port 3
		0b00011000  // port 4
	};
	
	while (1) 
    {
		for (int i = 0; i < 10; i++) {
			
			// Select digit
			if ((PINC & 0b00010000) == 0) { // 5th bit has low voltage
				selectDigit();
			}
			
			// Set a digit
			PORTC = ports[digit];
			
			// Set a number
			PORTB = digits[i];
			
			// Wait some time
			_delay_ms(500);
		}
    }
}

void selectDigit() {
	switch (digit) {
		
		case 0:
			digit = 1;
		break;
		
		case 1:
			digit = 2;
		break;
		
		case 2:
			digit = 3;
		break;
		
		case 3:
			digit = 0;
		break;
		
		default: // no-op
		break;
	}
}

