#-*- coding: utf-8 -*-

templ_case1="""
loglevel WARNING

N 30
alpha 199  # temps moyen passé en SC
gamma 1   # temps moyen pour transmettre un message entre deux noeuds
beta %d # Temps moyen qu'un noeud attend avand de redemander la SC
endtime %d # hour (time unit in ms)

simulation.endtime endtime
network.size N

protocol.uniform UniformRandomTransport
protocol.uniform.mindelay gamma * (1.0 - 0.1)
protocol.uniform.maxdelay gamma * (1.0 + 0.1)

protocol.naimitrehel NaimiTrehelAlgo
protocol.naimitrehel.transport uniform
protocol.naimitrehel.timeCS alpha
protocol.naimitrehel.timeBetweenCS beta

control.observer NaimiTrehelObserver
control.observer.experience case1_%d
control.observer.protocol naimitrehel
control.observer.step 1

#control.finalizer NaimiTrehelFinalizer
#control.finalizer.task naimitrehel
#control.finalizer.at -1
#control.finalizer.FINAL
"""

templ_case2="""
loglevel WARNING

N 30
alpha 100  # temps moyen passé en SC
gamma 100   # temps moyen pour transmettre un message entre deux noeuds
beta %d # Temps moyen qu'un noeud attend avand de redemander la SC
endtime %d # hour (time unit in ms)

simulation.endtime endtime
network.size N

protocol.uniform UniformRandomTransport
protocol.uniform.mindelay gamma * (1.0 - 0.1)
protocol.uniform.maxdelay gamma * (1.0 + 0.1)

protocol.naimitrehel NaimiTrehelAlgo
protocol.naimitrehel.transport uniform
protocol.naimitrehel.timeCS alpha
protocol.naimitrehel.timeBetweenCS beta

control.observer NaimiTrehelObserver
control.observer.experience case2_%d
control.observer.protocol naimitrehel
control.observer.step 1

#control.finalizer NaimiTrehelFinalizer
#control.finalizer.task naimitrehel
#control.finalizer.at -1
#control.finalizer.FINAL
"""

templ_case3="""
loglevel WARNING

N 30
alpha 20  # temps moyen passé en SC
gamma 180   # temps moyen pour transmettre un message entre deux noeuds
beta %d # Temps moyen qu'un noeud attend avand de redemander la SC
endtime %d

simulation.endtime endtime
network.size N

protocol.uniform UniformRandomTransport
protocol.uniform.mindelay gamma * (1.0 - 0.1)
protocol.uniform.maxdelay gamma * (1.0 + 0.1)

protocol.naimitrehel NaimiTrehelAlgo
protocol.naimitrehel.transport uniform
protocol.naimitrehel.timeCS alpha
protocol.naimitrehel.timeBetweenCS beta

control.observer NaimiTrehelObserver
control.observer.experience case3_%d
control.observer.protocol naimitrehel
control.observer.step 1

#control.finalizer NaimiTrehelFinalizer
#control.finalizer.task naimitrehel
#control.finalizer.at -1
#control.finalizer.FINAL
"""

T = 1000000
betas = [1, 2, 5, 20, 50, 200, 500, 1000, 1600, 2500, 4000, 5000, 7500, 10000, 25000, 50000, 75000, 100000, 130000, 200000]
#betas = [1, 2, 5, 20, 50, 200, 500, 1000, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1850, 1900, 1910, 1925, 1930, 1940, 1950, 2000, 2100, 2125, 2250, 2500, 3000, 3100, 3200, 3300, 3500, 3600, 3700, 3800, 3900, 4000, 4500, 5000, 5100, 5200, 5300, 5400, 5500, 5700, 5800, 6000, 6200, 6500, 6600, 6700, 6800, 7000, 8000, 10000, 15000, 25000, 50000, 75000, 100000, 130000, 200000]


# Case 1
for i in  betas:
    filename = "config/nt_1_%d.txt" % i
    with open(filename, 'w') as fh:
        fh.write(templ_case1 % (i, T, i));

# Case 2
for i in betas :
    filename = "config/nt_2_%d.txt" % i
    with open(filename, 'w') as fh:
        fh.write(templ_case2 % (i, T, i));

# Case 3
for i in betas :
    filename = "config/nt_3_%d.txt" % i
    with open(filename, 'w') as fh:
        fh.write(templ_case3 % (i, T, i));

for beta in betas: print(beta, end=" ")
