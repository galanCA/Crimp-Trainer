import socket
import time
import datetime as dt 
import matplotlib.pyplot as plt 
import matplotlib.animation as animation
from random import uniform

TCP_IP = '192.168.0.114'
TCP_PORT = 3300
BUFFER_SIZE = 20

def main():
	# Create figure for plotting
	fig = plt.figure()
	ax = fig.add_subplot(1,1,1)
	xs = []
	ys = []
	ys_max = 0
	
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.bind((TCP_IP, TCP_PORT))
	s.listen(1)

	conn, addr = s.accept()
	try:
		ani = animation.FuncAnimation(fig, animate, fargs=(conn, xs, ys, ys_max, ax), interval=100)
		plt.show()

	except KeyboardInterrupt:
		conn.close()


def animate(i, conn, xs, ys, ys_max, ax):
	rawData = conn.recv(BUFFER_SIZE)
	if len(rawData) is not 1:
		return None
	data = int.from_bytes(rawData, byteorder='big')
	print(rawData, data)
	xs.append(i)
	ys.append(data)
	xs = xs[-20:]
	ys = ys[-20:]
	ax.clear()
	ax.plot(xs, ys)
	ys_max = max(max(ys), ys_max)
	#print(ys_max)

if __name__ == '__main__':
	main()