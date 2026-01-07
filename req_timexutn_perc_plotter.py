# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import statistics

from sys import argv

if len(argv) < 4 :
    print("fuck you")
    exit(2)

req_time_f = argv[1]
utn_f = argv[2]
title = argv[3]

fig, ax1 = plt.subplots()

ax1.set_xlabel("Rho")
ax1.set_ylabel("Time units")

rhos = list()
data = list()

with open(req_time_f, "r") as f:
    lines = [line.strip() for line in f if line]

    for i in range(0, len(lines), 2):
        rho = float(lines[i])
        req_tim = [float(x) for x in lines[i + 1].split(",")]

        rhos.append(rho)
        data.append(req_tim)

    rhos_sorted, data_sorted = zip(*sorted(zip(rhos, data)))

    rhos = list(rhos_sorted)
    data = list(data_sorted)

    means = [statistics.mean(d) for d in data]
    stds  = [statistics.stdev(d) for d in data]

    ax1.errorbar(
        rhos,
        means,
        yerr=stds,
        fmt='-o',
        capsize=5,
        color="red"
    )


rhos = list()
data = list()

ax2 = ax1.twinx()
ax2.set_ylabel("%")

with open(utn_f, "r") as f:
    lines = [line.strip() for line in f if line]

    for i in range(0, len(lines), 2):
        rho = float(lines[i])
        datum = [float(x) for x in lines[i + 1].split(",")]
        rhos.append(rho)
        data.append(datum)

    rhos_sorted, data_sorted = zip(*sorted(zip(rhos, data)))
    rhos = list(rhos_sorted)
    data = list(data_sorted)
    plt.plot(rhos, data_sorted, '--')

ax2.set_ylabel("%")
ax2.legend(labels=["U", "T", "N"], loc="upper right")
plt.xscale("log")
plt.title(title)

plt.show()

