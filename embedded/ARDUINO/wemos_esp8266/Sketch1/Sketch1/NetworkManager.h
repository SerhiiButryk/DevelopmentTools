#pragma once

// #include <ESP8266WiFi.h> - For debug logging
#include <IPAddress.h>
#include <ESP8266WiFiAP.h>
#include <ESP8266WebServer.h>

#include <array>

namespace Car {

	class NetworkManager
	{
		public:
		
			void setupSoftAP();
			void setHandler(const char* str, UpdaterClass::THandlerFunction handler);
			void startServer();
			
			void handleClient();
			
			void sendOkResponse(const char* str);
			std::array<int, 2> getArgsForMotorControll();
			
			void logState();

		private:
			ESP8266WiFiAPClass wifi;
			ESP8266WebServer server; // Operates on port 80
	};

}
