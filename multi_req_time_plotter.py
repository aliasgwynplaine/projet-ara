# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import statistics
from sys import argv

if len(argv) < 4 :
    print("fuck you")
    exit(2)

plt.rcParams.update({'font.size': 20})


filename1 = argv[1]
filename2 = argv[2]
filename3 = argv[3]

for filename in [filename1, filename2, filename3] :
    rhos = list()
    data = list()
    
    with open(filename, "r") as f:
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

    plt.errorbar(
        rhos,
        means,
        yerr=stds,
        fmt='-*',
        capsize=5,
    )

plt.xlabel("ρ")
plt.xscale("log")
plt.ylabel("Time units")
plt.title("Average Requesting Time Per Node Per CS - N = 30")
plt.legend(labels=["γ << α", "γ == α", "γ >> α"], loc="lower right")
plt.hlines(200*(30-1), 0, 1000000, linestyles='--', color="blue")
plt.text(1e-2, 200*(30-1) + 150, "(α + γ)*(N - 1)" , ha='left', va='center', color="blue")

plt.show()

