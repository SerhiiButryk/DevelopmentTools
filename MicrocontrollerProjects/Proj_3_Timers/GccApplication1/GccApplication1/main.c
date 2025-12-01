// 1 MHz frequency for atmega8a
#define F_CPU 1000000UL

#include <avr/io.h>

static uint8_t counter = 0;

// Configuring of Timer/Counter0
// By default all timers are disabled
void setupTimer0();

// Calculation of Timer/Counter0 value
void countTimer0();


// Configuring of 16-bit Timer/Counter1
// By default all timers are disabled
void setupTimer1();

// Calculation of 16-bit Timer/Counter1 value
void countTimer1();

int main(void)
{
	setupTimer1();
	
	// Setup PC 0-2 for output mode 
	DDRC |= (1 << 0) | (1 << 1) | (1 << 2); 	
	
    while (1) 
    {
		countTimer1();
		
		// Time is elapsed, light the bulb !
		if (counter == 5) {
			PORTC |= (1 << 0) | (1 << 1) | (1 << 2);
		} else {
			PORTC = 0;
		}
		
    }
}

void setupTimer0() {
	// Timer/Counter Control Register where we can
	// enable timer and set prescaler
	TCCR0 = 0; // Reset register
	TCCR0 |= (1 << 0) | (1 << 2); // Select 1024 prescaler value
	
	// 8-bit register where we check the actual value of timer
	// during runtime.
	TCNT0 = 0; // Reset register
}

void countTimer0() {
	// Means MAX value of 8-bit timer
	if (TCNT0 == 0xff) {
		TCNT0 = 0;
		// With 1024 prescaler time is ~ 1.2 sec
		if (counter == 5) {
			// 1 sec is elapsed, reset
			counter = 0;
		} else {
			counter++;
		}
	}
}

void setupTimer1() {
	// 16-bit Timer/Counter1 Control Register where we can
	// enable timer and set prescaler
	TCCR1B = 0;
	TCCR1B |= (1<<1); // Select 8 prescaler value 
	
	// 16-bit register where we check the actual value of timer
	// during runtime.
	TCNT1 = 0; // Reset register
}

void countTimer1() {
	// Means 1/2 of MAX value of 16-bit timer
	if (TCNT1 == 0x00ff) {
		// With 8 prescaler time is ~ 2.2 sec
		if (counter == 0) {
			// 2.2 sec is elapsed, reset
			counter = 5;
		} else {
			counter = 0;
		}
	}
	// Reset register
	if (TCNT1 == 0xffff) {
		TCNT1 = 0;	
	}
}
