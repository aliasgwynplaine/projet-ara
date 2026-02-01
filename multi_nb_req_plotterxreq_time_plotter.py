# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import statistics

from sys import argv

if len(argv) < 3 :
    print("fuck you")
    exit(2)

plt.rcParams.update({'font.size': 20})

filenamereq1 = argv[1]
filenameret1 = argv[2]


fig, ax1 = plt.subplots()

rhos = list()
data = list()
nb_noeud = list()

with open(filenamereq1, "r") as f:
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
ln1 = ax1.plot(rhos, means, '-', c='red', label="Nb request per CS", linewidth=2.5)


ax1.set_xlabel("ρ")
ax1.set_ylabel("Nb", color="red")
ax2 = ax1.twinx()

rhos = list()
data = list()

with open(filenameret1, "r") as f:
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

ln2 = ax2.plot(
    rhos,
    means,
    '-',
    label="Time in requesting status",
    linewidth=2.5
)

ax2.set_ylabel("Time units", color="blue")
plt.xscale("log")
plt.title("Nombre de request per CS et Temps passé dans le 'état requesting")

lns = ln1 + ln2
labs = [l.get_label() for l in lns]
plt.legend(lns, labs, loc="lower right")
ax2.text(15, 200*(30-1) + 100, "(α + γ)*(N - 1)" , ha='left', va='center', color="blue")
ax2.hlines(200*(30-1), 0, 1000000, linestyles='--', color="blue")

ax1.set_ylim(-20, 600)
ax1.text(27e-3, 0, "0.027" , ha='left', va='center', color="gray")
ax1.text(0.125, 0, "0.125" , ha='left', va='center', color="gray")
ax1.vlines(27e-3, -50, 600, linestyles='--', color='gray')
ax1.vlines(0.125, -50, 600, linestyles='--', color='gray')


plt.show()