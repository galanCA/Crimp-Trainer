import time
import datetime as dt 
import matplotlib.pyplot as plt 
import matplotlib.animation as animation
from random import uniform


# Create figure for plotting
fig = plt.figure()
ax = fig.add_subplot(1,1,1)
xs = []
ys = []
ys_max = 0

def animate(i, xs, ys, ys_max):

	xs.append(i)
	ys.append(uniform(0,100))
	xs = xs[-20:]
	ys = ys[-20:]
	ax.clear()
	ax.plot(xs, ys)
	ys_max = max(max(ys), ys_max)
	print(ys_max)

ani = animation.FuncAnimation(fig, animate, fargs=(xs,ys, ys_max), interval=500)
plt.show()