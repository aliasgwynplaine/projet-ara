#-*- coding: utf-8 -*-

print("fuck you")
exit(0)
templ_case1="""
loglevel WARNING

N 30
alpha 199  # temps moyen passé en SC
gamma 1   # temps moyen pour transmettre un message entre deux noeuds
beta %d # Temps moyen qu'un noeud attend avand de redemander la SC
endtime %d # hour (time unit in ms)

random.seed 1337
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

random.seed 1337
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

random.seed 1337
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
betas = [1, 2, 4, 20, 50, 100, 500, 1000, 5000, 10000, 50000, 100000]

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
