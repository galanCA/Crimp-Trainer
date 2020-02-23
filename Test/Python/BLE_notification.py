import bluetooth

devices = bluetooth.discover_devices()

print("Devices found: %s " % len(devices))

for item in devices:
	print(item)