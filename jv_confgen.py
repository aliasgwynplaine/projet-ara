#-*- coding: utf-8 -*-
import random

templat="""
loglevel SEVERE
N 30
probacrash 1
alpha 199  # temps moyen passé en SC
gamma 1    # temps moyen pour transmettre un message entre deux noeuds
beta  %d # Temps moyen qu'un noeud attend avand de redemander la SC
freqR %d
endtime 100000

simulation.endtime endtime
network.size N

protocol.uniform UniformRandomTransport
protocol.uniform.mindelay gamma * (1.0 - 0.1)
protocol.uniform.maxdelay gamma * (1.0 + 0.1)

protocol.fifo FIFOTransport
protocol.fifo.transport uniform

protocol.naimitrehel NaimiTrehelAlgoCheckpointable
protocol.naimitrehel.timeCS alpha
protocol.naimitrehel.timeBetweenCS beta

protocol.juangvenkatesan JuangVenkatesanAlgo
protocol.juangvenkatesan.transport fifo
protocol.juangvenkatesan.checkpointable naimitrehel
protocol.juangvenkatesan.timecheckpointing freqR

protocol.naimitrehel.transport juangvenkatesan

control.crashcntrl CrashControler
control.crashcntrl.probacrash probacrash
control.crashcntrl.checkpointer juangvenkatesan
control.crashcntrl.faulty_nodes %d # Separated by '_'. replace w random
control.crashcntrl.at %d

init.ini JuangVenkatesanAlgoInitializer
init.ini.task juangvenkatesan

control.observer JuangVenkatesanObserver
control.observer.experience case0
control.observer.timecheckpointing freqR
control.observer.protocol naimitrehel
control.observer.checkpointer juangvenkatesan
control.observer.step 1
"""

betas = range(1500, 8000, 500)

# Case 1
for b in betas:
    for f in range(500, 10500, 500) : 
        filename = "config/jv_%d_%d.txt" % (b, f)
        with open(filename, 'w') as fh:
            fh.write(templat % (b, f, random.randint(0, 29), random.randint(85000, 95000)));

for beta in betas: print(beta, end=" ")
