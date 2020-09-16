// Libraries
#include "HX711.h"
#include <WiFi.h>

/** WiFi constants **/
// Router definitions
const char * networkName = "IRLab";
const char * networkPswd = "robots4ever";

// Host
const char * host = "192.168.50.40";
const int port = 3300;

/** Sensor definitions **/ 
#define calibration_factor -7050.0

#define DOUT 17
#define CLK 16

// Variable definition
WiFiClient client;
HX711 scale;
long data;

void setup() {
  Serial.begin(115200);
  // Connect to router
  connectToWiFi(networkName, networkPswd);

  // Connect to server
  connectToServer(host, port);

  //setup load sensor
  scale.begin(DOUT, CLK);
  scale.set_scale(calibration_factor);
  scale.tare();
  
}

void loop() {
  sensorData();
  client.write(data);

  delay(500);

}

void sensorData() {
  data = scale.get_units();
  Serial.println(data);
}

void connectToWiFi(const char * ssid, const char * pwd) {
  int ledState = 0;

  printLine();
  Serial.println("Connecting to WiFi network: " + String(ssid));
  WiFi.begin(ssid, pwd);

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    //digitalWrite(LED_PIN, ledState);
    //ledState = (ledState + 1) % 2;
    delay(500);
  }
  
  Serial.println();
  Serial.println("connected to WiFi");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void connectToServer(const char * IP, const int Port) {
  if (client.connect(IP, Port)) {
    Serial.println("Server connected");
    client.flush();
  }
}

void printLine() {
  Serial.println();
  for (int i = 0; i<30; i++)
    Serial.print("-");
  Serial.println();
}
