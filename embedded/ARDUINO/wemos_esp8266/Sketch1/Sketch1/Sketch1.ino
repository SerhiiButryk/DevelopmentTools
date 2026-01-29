/*
 * LOLIN(WeMos) esp8266 D1 R1 arduino compatible board.
 *
 * Build process:
 * https://docs.arduino.cc/arduino-cli/sketch-build-process/
 *
 * WeMos docs:
 * https://www.wemos.cc/en/latest/tutorials/d32/get_started_with_arduino_d32.html
 * https://arduino-esp8266.readthedocs.io/en/latest/esp8266wifi/readme.html
 *
 * Arduino framework docs:
 * https://docs.arduino.cc/programming/?_gl=1*ives24*_up*MQ..*_ga*NTE2NjY5ODYxLjE3Njk3MDAyMjQ.*_ga_NEXN8H46L5*czE3Njk3MDMzNzAkbzIkZzAkdDE3Njk3MDMzNzAkajYwJGwwJGgxNTA3OTgzMzA1
 *
 */ 

void setup()
{
	pinMode(LED_BUILTIN, OUTPUT);
}

void loop()
{
	digitalWrite(LED_BUILTIN, LOW);
	delay(1000);                   
	digitalWrite(LED_BUILTIN, HIGH);
	delay(2000);
}
