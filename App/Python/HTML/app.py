from flask import Flask, render_template, request, jsonify, Response
import random
from datetime import datetime
import json
import time
import socket

app = Flask(__name__)
random.seed()

TCP_IP = '192.168.0.114'
TCP_PORT = 3300
BUFFER_SIZE = 20
seccion = {'connected' : False,
			'conn': None}

@app.route('/', methods=['GET'])
def index():
	message = random.randint(1,100)
	return render_template('index.html', message=message)

@app.route('/findESP32')
def findESP32():
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.bind((TCP_IP, TCP_PORT))
	s.listen(1)
	print("connecting...")
	conn, addr = s.accept()
	seccion['conn'] = conn
	seccion['connected'] = True
	print("Connected")
	return ("nothing")

@app.route('/chart-data', methods=['GET'])
def chart_data():
	def generate_random_data():
		if seccion['connected']:
			while True:
				rawData = seccion['conn'].recv(BUFFER_SIZE)
				print(rawData)
				if len(rawData) is not 1:
					continue
				data = int.from_bytes(rawData, byteorder='big')
				json_data = json.dumps(
					{'time':datetime.now().strftime('%H:%M:%S:%f'), 'value':data })
				yield f"data:{json_data}\n\n"
				time.sleep(.1)

	return Response(generate_random_data(), mimetype='text/event-stream')	


if __name__ == '__main__':
	app.run(debug=True, host='192.168.0.114',port=6600)