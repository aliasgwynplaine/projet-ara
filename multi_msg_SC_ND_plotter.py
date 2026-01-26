# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np

from sys import argv

if len(argv) < 4 :
    print("fuck you")
    exit(2)

filename1 = argv[1]
filename2 = argv[2]
filename3 = argv[3]

for filename in [filename1, filename2, filename3] :
    rhos = list()
    data = list()

    with open(filename, "r") as f:
        lines = [line.strip() for line in f if line]

    for i in range(0, len(lines), 2):
        rho = float(lines[i].strip())
        nb_msg = [float(x) for x in lines[i + 1].split(",")]
        rhos.append(rho)
        data.append(nb_msg)

    rhos_sorted, data_sorted = zip(*sorted(zip(rhos, data)))

    rhos = list(rhos_sorted)
    data = list(data_sorted)

    mpcs = [np.mean(d) for d in data]
    std  = [np.sqrt(np.mean((np.array(d) - m)**2)) for d, m in zip(data, mpcs)]

    plt.plot(rhos, mpcs, '-o')

plt.xlabel("Rho")
plt.xscale("log")
plt.ylabel("NB messages")
plt.title("AVG Number of Messages Per CS Per Noued - N = 30")
plt.legend(labels=["γ << α", "γ == α", "γ >> α"])

plt.show()

