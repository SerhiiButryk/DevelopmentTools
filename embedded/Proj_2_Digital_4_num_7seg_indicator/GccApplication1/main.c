/************************************************************************/
/* Atmega8a program sample */
/* Author: Serhii Butryk   */                                                          
/************************************************************************/

// 1 MHz frequency 
#define F_CPU 1000000UL

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

static uint8_t segmentIndex = 0;
static uint16_t num = 0; // Max is 9999, so values are less than 9999

// Handle interrupt event on Timer/Counter0 overflow
ISR(TIMER0_OVF_vect) {
	
	// Show next segment
	segmentIndex++;
	if (segmentIndex >= 4) { segmentIndex = 0; }
	
	// Count display value
	num++;
	
	if (num >= 9999) {
		num = 0;
	}
	
	// Reset timer value register to make sure that we start correctly
	TCNT0 = 0;
}

void display7SegCommonCathode(int num);
void display7SegCommonAnode(int num);

// Handle read/write operations in EEPROM

////////////////////////////////////////////////////////
// Check bound if you need to store a big value
////////////////////////////////////////////////////////

void write(uint16_t addr, uint16_t data); // max addr is 512 (decimal number)
uint16_t read(uint16_t addr); // max addr is 512 (decimal number)

uint16_t getNumberFromMemory(); // result is not more than 9999 or < 9999
void storeNumberInMemory(uint16_t num); // 'num' is not more than 9999 or < 9999

int main()
{
	// Interruption setup
	// The steps are the following:
	// 1. Enable global interruption flag.
	// 2. Select and enable specific interruption. 
	
	// For example, if we want to use external interruption on INT0 PIN.
	// If the I-bit in SREG and the INT0 bit in GICR are set (one), 
	// the MCU will jump to the corresponding Interrupt Vector. 
	// In our case it is ISR(INT0_vect) function.
	
	// Enable global interruption by setting 7 bit
	SREG |= (1<<7);
	
	// Enable individual interrupt on Timer/Counter0 overflow
	TIMSK |= (1<<0);
	
	// Enable Timer/Counter0 with 1024 prescale (0b101)
	TCCR0 |= (1<<0) | (1<<2);
	TCCR0 &= ~(1<<1);  
	
	// Reset timer value register to make sure that we start correctly
	TCNT0 = 0;
	
	// Set 7 PB pins for output, will be controlling a, b, c ... dp segments
	DDRD = 0b11111111;
	// Set 3 PC pins for output, will be controlling d1, d2 ... d4
	DDRB = 0b00001111;
	
	num = getNumberFromMemory();
	
	while (1) 
    {
		storeNumberInMemory(num);
		
		display7SegCommonCathode(num);
    }
}

void display7SegCommonCathode(int num) {
	
	uint8_t digits [] = { 0x3f, 0x6, 0x5b, 0x4f, 0x66, 0x6d, 0x7d, 0x7, 0x7f, 0x6f };
	
	// Corresponds to 4 ports of 4 digit segment display
	uint8_t segments[] = {
		0b00000001, // seg 1
		0b00000010, // seg 2
		0b00000100, // seg 3
		0b00001000  // seg 4
	};
	
	// Calculate every digit
	uint8_t digit4 = num % 10; num /= 10;
	uint8_t digit3 = num % 10; num /= 10;
	uint8_t digit2 = num % 10; num /= 10;
	uint8_t digit1 = num % 10;
	
	uint8_t allNumbers[] = { digit1, digit2, digit3, digit4 };
	
	uint8_t index = allNumbers[segmentIndex];
	PORTD = digits[index];
	
	PORTB = segments[segmentIndex];
	
}

void display7SegCommonAnode(int num) {
	
	// Corresponds to a,b,c,d,e,f,g,dp segments of single segment of a display
	uint8_t digits[] = {
		0b11111111, // 0        a
		0b11111001, // 1        _
		0b10100100, // 2    f | g | b 
		0b10110000, // 3        -
		0b10011001, // 4    e | d | c
		0b10010010, // 5        - . dp
		0b10000010, // 6
		0b11111000, // 7
		0b10000000, // 8
		0b10010000  // 9
	};
	
	// Corresponds to 4 ports of 4 digit segment display
	uint8_t segments[] = {
		0b00000001, // seg 1
		0b00000010, // seg 2
		0b00000100, // seg 3
		0b00001000  // seg 4
	};
	
	// Calculate every digit
	uint8_t digit4 = num % 10; num /= 10;
	uint8_t digit3 = num % 10; num /= 10;
	uint8_t digit2 = num % 10; num /= 10;
	uint8_t digit1 = num % 10;
	
	uint8_t allNumbers[] = { digit1, digit2, digit3, digit4 };
	
	uint8_t index = allNumbers[segmentIndex];
	PORTD = digits[index];
	
	PORTB = segments[segmentIndex];
	
}

void write(uint16_t addr, uint16_t data) {
	
	/*
		1. Disable global interrupts (write can fail at the moment of another interruption).
		2. Wait until EEWE becomes zero (meaning all write operations are completed 
		and we can start new write for the next bit)
		3. Write new EEPROM address to EEAR.
		4. Write new EEPROM data to EEDR.
		5. Write a logical one to the EEMWE bit.
		6. Write a logical one to the EEWE bit.
		7. Restore global interrupts.
		
		Registers:
		EECR - The EEPROM Control Register
		EEAR - The EEPROM Address Register
		EEDR – The EEPROM Data Register
	*/
	
	while(EECR & (1<<EEWE));
	
	// Disable global interruption
	SREG &= ~(1<<7);
	
	EEAR = addr;
	EEDR = data;
	
	EECR |= (1<<EEMWE);
	EECR |= (1<<EEWE);
	
	// Restore global interruption
	SREG |= (1<<7);
	
}

uint16_t read(uint16_t addr) {
	
	/*
		1. Wait until EEWE becomes zero (meaning all write operations are completed 
		and we can start new write for the next bit)
		2. Write new EEPROM address to EEAR.
		3. Write a logical one to the EERE bit (enabling the read access).
		4. Read from EEPROM EEDR our data.
		
		Registers:
		EECR - The EEPROM Control Register
		EEAR - The EEPROM Address Register
		EEDR – The EEPROM Data Register
	*/
	
	while(EECR & (1<<EEWE));
	
	EEAR = addr;
	
	EECR |= (1<<EERE);
	
	return EEDR;
}

uint16_t getNumberFromMemory() {
	
	uint8_t digit1 = read(0x0); // Should be less than 10 or 0
	uint8_t digit2 = read(0x1); // Should be less than 10 or 0
	uint8_t digit3 = read(0x2); // Should be less than 10 or 0
	uint8_t digit4 = read(0x3); // Should be less than 10 or 0
	
	return digit1 * 1000 + digit2 * 100 + digit3 * 10 + digit4;
}

void storeNumberInMemory(uint16_t num) {
	
	// Suppose we handle 4 digit numbers
	
	// Calculate every digit
	uint8_t digit4 = num % 10; num /= 10;
	uint8_t digit3 = num % 10; num /= 10;
	uint8_t digit2 = num % 10; num /= 10;
	uint8_t digit1 = num % 10;
	
	// Every byte will have every digit for simplicity
	write(0x0, digit1);
	write(0x1, digit2);
	write(0x2, digit3);
	write(0x3, digit4);
	
}

