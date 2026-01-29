/************************************************************************/
/* Atmega8a program sample */
/* Author: Serhii Butryk   */
/************************************************************************/

// 1 MHz frequency
#define F_CPU 1000000UL 

#include <avr/io.h>
#include <util/delay.h>

/*
	Program for ATmega8A-PU
	
	There are 3 registers for PB0-PB7, PC0-PC6 and PD0-PD7 pins: 
	DDRB – The Port B Data Direction Register
	DDRC – The Port C Data Direction Register
	DDRD – The Port D Data Direction Register 
*/

#define _LED_LIGHT_TIME_ 300

void enable_DDRC(short Nbit);
void enable_PortC_HighVoltage(short Nbit);
void reset_PortC_Voltage();
void delay();

void lightLEDsOderOne();
void lightLEDsOderTwo();

int main(void)
{	
	// Set PC0 for an output 
	enable_DDRC(0);
	// Set PC1 for an output
	enable_DDRC(1);
	
	// Set PB pins for an input
	DDRB = 0b00000000;
	// Enable pull-up resistor for PB0
	PORTB = 0b00000001;
	
	while (1) 
    {
		// Check if we have low voltage on PB0 pin 
		// which means that button is clicked
		if ((PINB & 0b00000001) == 0) {	
			lightLEDsOderOne();
			delay();
		} else {
			reset_PortC_Voltage();
		}	
    }
	
}

void lightLEDsOderOne() {
	// Light first LED
	enable_PortC_HighVoltage(0);
	// Light second LED
	enable_PortC_HighVoltage(1);
}

void lightLEDsOderTwo() {
	reset_PortC_Voltage();
	delay();
	// Light first LED
	enable_PortC_HighVoltage(0);
	// Light second LED
	enable_PortC_HighVoltage(1);
	delay();
}

void enable_DDRC(short n) {
	// Enable register for PC n pin
	DDRC |= 1 << n;
}

void enable_PortC_HighVoltage(short n) {
	// Set high voltage for PC n pin
	PORTC |= 1 << n;
}

void reset_PortC_Voltage() {
	// Reset PORTC to '0'
	PORTC &= 0;
}

void delay() {
	_delay_ms(_LED_LIGHT_TIME_);
}
