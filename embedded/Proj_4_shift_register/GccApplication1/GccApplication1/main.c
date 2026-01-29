/************************************************************************/
/* Atmega8a program sample */
/* Author: Serhii Butryk   */
/************************************************************************/

// 1 MHz frequency
#define F_CPU 1000000UL

#include <avr/io.h>
#include <util/delay.h>

int main(void)
{
	/*
		PC0 is for controlling shift register clock (SH_CP pin)
		PC1 is for serial data in (SD pin)   
		PC2 is for storage register clock (ST_CP)   
		PC3 is for mater reset (MR)                                                             
	*/
	
	// Setup PC 0-3 for output mode 
	DDRC |= (1<<0) | (1<<1) | (1<<2) | (1<<3);
	
    while (1) 
    {
		
		PORTC |= (1<<3); // Enable the register
	
		_delay_ms(1000); // This delay sets time when LEDs are off
		
		// Sends (1111) to shift register
		
		PORTC |= (1<<1); // Serial data has logical 1
		
		/// Clock ///
		
		for (int i = 0; i < 4; i++) {
			PORTC |= (1<<0); // Shift register clock HIGH signal
			PORTC &= ~(1<<0); // Shift register clock LOW signal
		}
		
		/////////////
		
		// Sends (0000) to shift register
		
		PORTC &= ~(1<<1); // Serial data has logical 0
		
		/// Clock ///
		
		for (int i = 0; i < 4; i++) {
			PORTC |= (1<<0); // Shift register clock HIGH signal
			PORTC &= ~(1<<0); // Shift register clock LOW signal
		}
		
		/////////////
		
		// Sending data from register to outputs
		
		PORTC |= (1<<2); // Storage register clock HIGH signal
		PORTC &= ~(1<<2); // Storage register clock LOW signal
		
		// Clearing register
		
		PORTC &= ~(1<<3);
	
		_delay_ms(1000); // This delay sets time when LEDs are on
		
		// Sending data from register to outputs
		
		PORTC |= (1<<2); // Storage register clock HIGH signal
		PORTC &= ~(1<<2); // Storage register clock LOW signal
		
    }
}

