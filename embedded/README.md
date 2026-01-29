## Dev notes

# Atmel Studio

Debugging:

https://www.youtube.com/watch?v=RF4-NB776ro

# Arduino

IDE locations:

https://support.arduino.cc/hc/en-us/articles/4415103213714-Find-sketches-libraries-board-cores-and-other-files-on-your-computer

Arduino core platform:

https://github.com/arduino/ArduinoCore-avr

Docs:

https://docs.arduino.cc/language-reference/?_gl=1*15dsyg*_up*MQ..*_ga*MTQ1ODMwNTc2NC4xNzY5NzA2MDM5*_ga_NEXN8H46L5*czE3Njk3MDYwMzYkbzEkZzAkdDE3Njk3MDYwMzYkajYwJGwwJGg4MDI0ODU1NA..

# WeMos esp8266 Board

Core platform:

https://github.com/esp8266/Arduino

Docs:

https://www.wemos.cc/en/latest/tutorials/d32/get_started_with_arduino_d32.html

https://arduino-esp8266.readthedocs.io/en/latest/esp8266wifi/readme.html

Setup instructions with Atmel studio 

Step 1: Install Visual Micro for Atmel Studio 

Download and install "Arduino IDE for Atmel Studio" plugin.

https://www.visualmicro.com/page/Arduino-Visual-Studio-Downloads.aspx

Restart Atmel Studio to finalize the installation. 

Step 2: Configure ESP8266 Boards 

Open the Arduino IDE (separate installation) and go to File > Preferences.

Add the ESP8266 board manager URL to "Additional Boards Manager URLs": https://arduino.esp8266.com/stable/package_esp8266com_index.json.

In Arduino IDE, go to Tools > Board > Boards Manager, search for esp8266, and install it.

Close Arduino IDE and return to Atmel Studio.

In Atmel Studio, go to vMicro > Board Manager, select esp8266 from the list, and ensure the correct board (e.g., NodeMCU 1.0, Generic ESP8266) is selected. 

Step 3: Create a New Arduino Project using vMicro plugin. Click File > New > Project

Visual Micro will create a .ino file. You can start writing your code here. 

Step 4: Configure Port and Upload 
Connect your ESP8266 via the USB-to-Serial adapter.

In Atmel Studio, under the vMicro menu, select the correct COM port for your device.

To upload, put the ESP8266 into flash mode (Hold Flash/Program button, press Reset, release Flash). This is board specific step. For WeMos D1 R1 skip this step.

Click the Build & Upload button (green arrow). Check console output in Atmel Studio.

# WeMos D1 R1 specification

Модель: Контроллер WeMos ESP8266 Wi-Fi D1/R1
Интерфейс USB-UART: CH340
Чип: ESP-12F
Стандарт Wi-Fi: 802.11 b/g/n
Тактовая частота: 80 МГц
Flash-память: 4 МБ
Входное напряжение питания: 5 В
Через USB: 5 В
Через пин Vin: 7–12 В
Через DC Barrel Jack: 7–12 В
Напряжение логических уровней: 3,3 В
Контакты общего назначения:
Всего контактов общего назначения: 12
Контакты ввода-вывода GPIO: 11
Контакты с АЦП: 1
Контакты с ШИМ: 11
Аппаратные интерфейсы: 1×SPI и 1×I²C, 1×UART и 1×UART (только с TX)
