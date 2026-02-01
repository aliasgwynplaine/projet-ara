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
    nb_noeud = list()

    with open(filename, "r") as f:
        lines = [line.strip() for line in f if line]

    for i in range(0, len(lines), 2):
        rho, noeud = lines[i].split(",")
        rho = float(rho)
        noeud = float(noeud)
        nb_req = [float(x) for x in lines[i + 1].split(",")]
        rhos.append(rho)
        data.append(nb_req)
        nb_noeud.append(noeud)

    rhos_sorted, data_sorted = zip(*sorted(zip(rhos, data)))

    rhos = list(rhos_sorted)
    data = list(data_sorted)

    means = [sum(d) / nd for d, nd in zip(data, nb_noeud)]
    plt.plot(rhos, means, '-o')

plt.xlabel("ρ")
plt.xscale("log")
plt.title("Number of Requests Per Noeud - N = 30")
plt.legend(labels=["γ << α", "γ == α", "γ >> α"])

plt.show()