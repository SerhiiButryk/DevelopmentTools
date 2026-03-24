#include "NetworkManager.h"

#include <Arduino.h>

#include "CarController.h"

namespace Car {
	
	void NetworkManager::setupSoftAP() {
		
		// WeMos soft-AP own IP address
		const IPAddress local_IP(192,168,4,22);

		// IP address if someone wants to talk to external network,
		// however, in soft-AP mode it's just a placeholder,
		// it must be within the same subnet as local_IP
		const IPAddress gateway(192,168,4,9);

		// Subnet mask to differentiate between host and network part
		const IPAddress subnet(255,255,255,0);
		
		const char* SSID = "ESPsoftAP_01";
		
		Serial.print("Setting soft-AP configuration ... ");
		
		bool result = wifi.softAPConfig(local_IP, gateway, subnet);
		Serial.printf("Result = %d\n", result);
		
		if (!result) {
			Serial.printf("Stopped due to an error !\n");
			return;
		}

		Serial.print("Setting SSID configuration ... ");
		result = wifi.softAP(SSID);
		Serial.printf("Result = %d\n", result);
		
		if (!result) {
			Serial.printf("Failed to set soft AP SSID !\n");
		}
	}
	
	void NetworkManager::setHandler(const char* str, UpdaterClass::THandlerFunction handler) {
		server.on(str, handler);
	}
	
	void NetworkManager::startServer() {
		server.begin();
	}
	
	void NetworkManager::handleClient() {
		server.handleClient();
	}
	
	void NetworkManager::sendOkResponse(const char* str) {
		server.send(200, "text/html", str);
	}
	
	std::array<int, 2> NetworkManager::getArgsForMotorControll() {
		
		 std::array<int, 2> argsArr = {-1, -1};
		
		const String& uri = server.uri();
		
		if (server.args() == 2) {
			
			const String& optionsStr = server.arg("options");
			const String& speedStr = server.arg("speed");
			
			uint8_t optionsInt = static_cast<uint8_t>(optionsStr.toInt());
			uint8_t speedInt = static_cast<uint8_t>(speedStr.toInt());
			
			argsArr[0] = speedInt;
			argsArr[1] = optionsInt;
		}
		
		return argsArr;
	}
	
	void NetworkManager::logState() {
		const char* status = "Operating in SOFT_AP_MODE\n";
		String message(status);
		
		IPAddress ip = wifi.softAPIP();
		String wifiNameStr = wifi.softAPSSID();
		uint8_t stationNum = wifi.softAPgetStationNum();
		
		const char* idAddrStr = "AP IP address = ";
		message.concat(idAddrStr);
		message.concat(ip.toString());
		message.concat(", ");
		
		const char* idSSIDStr = "AP SSID = ";
		message.concat(idSSIDStr);
		message.concat(wifiNameStr);
		message.concat(", ");
		
		const char* stationsStr = "Stations connected = ";
		message.concat(stationsStr);
		message.concat(stationNum);
		message.concat(".");
		
		message.concat("\n");
		
		Serial.printf(message.c_str());
	}
	
}