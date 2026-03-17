/************************************************************************/
/* Atmega8a program sample */
/* Author: Serhii Butryk   */
/************************************************************************/

// 1 MHz frequency
#define F_CPU 1000000UL

#include <avr/io.h>

int main(void)
{
	// IO setup
	
	DDRB |= (1<<0) | (1<<1) | (1<<2);
	PORTB = 0;
	
	DDRC &= ~(1<<0); 
	
	// Enable ADC
	
	ADCSRA |= (1<<ADEN); 
	
	// Set free running select ADC mode. It means that we'll continuously measure 
	// the voltage on the input pin.
	
	ADCSRA |= (1<<ADFR);
	
	// Set prescaler select ADC (011) = 8
	
	ADCSRA &= ~(1<<ADPS2);
	ADCSRA |= (1<<ADPS1) | (1<<ADPS0);
	
	// Set internal 2.56 Voltage reference point
	
	ADMUX |= (1<<REFS1) | (1<<REFS0); 
	
	// Set 10-bit ADC register layout in right layout mode 
	
	ADMUX &= ~(1<<ADLAR); 
	
	// Tell that we want to measure signal on ADC0 pin (0000)
	
	ADMUX &= ~( (1<<MUX3) | (1<<MUX2) | (1<<MUX1) | (1<<MUX0) );
	 
	// Start ADC or start ADC conversion
	
	ADCSRA |= (1<<ADSC); 
	
    while (1) 
    {
		// Check whether ADC finished conversion
		if (ADCSRA & (1<<4)) {
			
			if (ADC > 600) {
				// Turn on LED 1
				PORTB = 0;
				PORTB |= (1<<0);
			}
			
			if (ADC >= 560 && ADC < 800) {
				// Turn on LED 2
				PORTB = 0;
				PORTB |= (1<<1);
			}
			
			if (ADC < 560) {
				// Turn on LED 3
				PORTB = 0;
				PORTB |= (1<<2);
			}
			
			// Reset bit
			ADCSRA |= (1<<4);
		}
		
    }
}

