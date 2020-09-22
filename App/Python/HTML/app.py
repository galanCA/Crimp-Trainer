from flask import Flask, render_template, request, jsonify, Response
import random
from datetime import datetime
import json
import time
import socket

app = Flask(__name__)
random.seed()


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
	print("Connected")
	return ("nothing")

@app.route('/chart-data', methods=['GET'])
def chart_data():
	def generate_random_data():
		while True:
			json_data = json.dumps(
				{'time':datetime.now().strftime('%Y-%m-%d %H:%M:%S'), 'value':random.random()*100 })
			yield f"data:{json_data}\n\n"
			time.sleep(0.5)

	return Response(generate_random_data(), mimetype='text/event-stream')	


if __name__ == '__main__':
	app.run(debug=True, host='192.168.0.114',port=6600)