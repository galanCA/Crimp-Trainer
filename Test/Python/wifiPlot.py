import socket

TCP_IP = '192.168.50.40'
TCP_PORT = 3000
BUFFER_SIZE = 20

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.listen(1)

conn, addr = s.accept()
try:
	while(1):
		data = conn.recv(BUFFER_SIZE)
		print ("received data:", data)

except KeyboardInterrupt:
	conn.close()
