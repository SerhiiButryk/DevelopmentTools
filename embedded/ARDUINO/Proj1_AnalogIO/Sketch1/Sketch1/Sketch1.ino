/* 
	Background info:
	
	There are analog input pins (AO) and analog output pins (PWD pins).
	The analogWrite() requires a duty cycle	value which is 0 - 255. 
	It's 1 byte. That's cause usually we work with 8 bit micro controllers.    
	So, it means that max value of 255 provides 100% of Voltage, 127 - 50% of Voltage
	and 0 - 0 Voltage.       
	
	This is an example with a potentiometer which is connected to A0, Vcc and GND pins.                                                      
*/

const uint8_t PIN = D8;

const uint8_t potentiometer = A0;

void setup()
{
	pinMode(PIN, OUTPUT);
	Serial.begin(9600);
}

void loop()
{		
	int value = analogRead(potentiometer);
	
	analogWrite(PIN, value);
	
	Serial.print(value);
	Serial.print("\n");
}
