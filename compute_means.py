# -*- coding: utf-8 -*-
from sys import argv
import statistics

if len(argv) < 2 :
    print("usage: {} <filename>")
    exit(1)

filename = argv[1]

nb_app = list()
nb_cs = list()
rtime = list()
nb_msg = list()
stocka = list()
ancienT = list()
ancienA = list()

with open(filename, 'r') as fh :
    lines = [line.strip() for line in fh if line]

    for line in lines :
        d = line.split(',')
        nb_app.append(int(d[0]))
        nb_cs.append(int(d[1]))
        rtime.append(int(d[2]))
        nb_msg.append(int(d[3]))
        stocka.append(int(d[4]))
        ancienT.append(int(d[5]))
        ancienA.append(int(d[6]))

beta = filename.split('_')[1]
freq = filename.split('_')[2].split('.')[0]
print("%s" % (freq), end='')

for m in nb_app, rtime, nb_msg, stocka, ancienT, ancienA :
    print(",{}".format(int(statistics.mean(m))), end='')
    print(",{}".format(int(statistics.stdev(m))), end='')

print(",", end="")
print(beta)