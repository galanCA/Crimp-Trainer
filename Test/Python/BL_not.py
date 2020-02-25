import asyncio
import time
from bleak import discover, BleakClient


@asyncio.coroutine
async def run():
	devices = await discover()
	return devices


loop = asyncio.get_event_loop()
devices_list = loop.run_until_complete(run())	

for d in devices_list:
	#print (d)
	if d.name == "Crimp Trainer":
		print(d.address)
		MACaddress = d.address
		break

try:
	MACaddress
except NameError:
	print ("No Device found")
	raise


MODEL_NBR_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8"

async def get_data(address, loop): 
	async with BleakClient(address, loop=loop) as client:
		try:
			while(1):
				model_number = await client.read_gatt_char(MODEL_NBR_UUID)
				#print(model_number)
				#print(model_number, model_number[0],model_number[1],model_number[2],model_number[3])
				print(int.from_bytes(model_number,"little"))
				#time.sleep(.1)
		except KeyboardInterrupt:
			await client.disconnect()
			print("Done")
		
loop_data = asyncio.get_event_loop()

loop_data.run_until_complete(get_data(MACaddress, loop_data))
'''
except KeyboardInterrupt:
	loop_data.stop()
	print("Done")
'''