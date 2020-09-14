#include <WiFi.h>

const char * networkName = "IRLab";
const char * networkPswd = "robots4ever";

//Host 
const char * host = "192.168.50.40";
const int port = 3300;

const int BUTTON_PIN = 0;
const int LED_PIN = 33;

const char ON_STM[] = "On";
const char OFF_STM[] = "Off";

char msg[20];
WiFiClient client;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  connectToWiFi(networkName, networkPswd);

  connectToServer(host, port);

  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, 0);
}

void loop() {
  client.write("hello From ESP32");
  /*
  delay(1000);
  while(1) {
    client.write(random(300));
    delay(1000);
  }
  */
}

void connectToServer(const char * IP, const int Port) {
  if (client.connect(IP, Port)){
    Serial.println("Serve connected");
    client.flush();
  }
}

void connectToWiFi(const char * ssid, const char * pwd) {
  int ledState = 0;

  printLine();
  Serial.println("Connecting to WiFi network: " + String(ssid));

  WiFi.begin(ssid, pwd);

  while ( WiFi.status() != WL_CONNECTED) {
    digitalWrite(LED_PIN, ledState);
    ledState = (ledState + 1) % 2;
    delay(500);
    Serial.print(".");
  }

  Serial.println();
  Serial.println("WiFi connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void printLine() {
  Serial.println();
  for (int i = 0; i <30; i++)
    Serial.print("-");
  Serial.println();
}
