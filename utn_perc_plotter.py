# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import statistics

from sys import argv

if len(argv) <3 :
    print("fuck you")
    exit(2)

plt.rcParams.update({'font.size': 20})

filename = argv[1]
title = argv[2]

rhos = list()
data = list()

with open(filename, "r") as f:
    lines = [line.strip() for line in f if line]

for i in range(0, len(lines), 2):
    rho = float(lines[i])
    datum = [float(x) for x in lines[i + 1].split(",")]
    rhos.append(rho)
    data.append(datum)

rhos_sorted, data_sorted = zip(*sorted(zip(rhos, data)))
rhos = list(rhos_sorted)
data = list(data_sorted)
plt.plot(rhos, data_sorted, '-')

plt.xlabel("ρ")
plt.xscale("log")
plt.ylabel("%")
plt.ylim(-5, 105)
plt.title(title)
plt.legend(labels=["U", "T", "N"], loc="upper right")

plt.show()

