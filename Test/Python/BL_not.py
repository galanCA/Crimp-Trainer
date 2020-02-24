import asyncio
from bleak import discover, BleakClient

@asyncio.coroutine
async def run():
	devices = await discover()
	#for d in devices:
	#	print(d)
	return devices


loop = asyncio.get_event_loop()
devices_list = loop.run_until_complete(run())	

for d in devices_list:
	if d.name == "Crimp Trainer":
		print(d.address)
		MACaddress = d.address
		break


MODEL_NBR_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8"

async def get_data(address, loop):
    async with BleakClient(address, loop=loop) as client:
        model_number = await client.read_gatt_char(MODEL_NBR_UUID)
        print(model_number[0])

        #print("Model Number: {0}".format("".join(map(chr, model_number))))

loop_data = asyncio.get_event_loop()
loop_data.run_until_complete(get_data(MACaddress, loop_data))
loop_data.run_until_complete(get_data(MACaddress, loop_data))

#loop_data.run_forever()