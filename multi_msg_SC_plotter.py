# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np

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
    nb_cs = list()

    with open(filename, "r") as f:
        lines = [line.strip() for line in f if line]

    for i in range(0, len(lines), 2):
        rho, cs = lines[i].split(",")
        rho = float(rho)
        cs = float(cs)
        nb_msg = [float(x) for x in lines[i + 1].split(",")]
        rhos.append(rho)
        data.append(nb_msg)
        nb_cs.append(cs)


    rhos_sorted, data_sorted, nb_cs_sorted = zip(*sorted(zip(rhos, data, nb_cs)))

    rhos = list(rhos_sorted)
    data = list(data_sorted)
    nb_cs = list(nb_cs_sorted)

    mpcs = [sum(d) / cs for d, cs in zip(data, nb_cs)]
    std  = [np.sqrt(np.mean((np.array(d) - m)**2)) for d, m in zip(data, mpcs)]

    plt.plot(rhos, mpcs, '-o')

plt.xlabel("ρ")
plt.xscale("log")
plt.ylabel("Nb Messages")
plt.ylim(1.8, 5)
plt.title("AVG Number of Messages Per CS per simulation - N = 30")
plt.legend(labels=["γ << α", "γ == α", "γ >> α"])

plt.show()

