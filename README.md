# projet - ara

the next files are given by the teachers:

```bash
ara/
├── projet
│   ├── checkpointing
│   │   ├── algorithm
│   │   │   └── JuangVenkatesanAlgo.java
│   │   ├── Checkpointable.java
│   │   ├── Checkpointer.java
│   │   ├── CrashControler.java
│   │   └── NodeState.java
│   └── mutex
│       ├── InternalEvent.java
│       ├── NaimiTrehelAlgoCheckpointable.java
│       └── NaimiTrehelAlgo.java
└── util
    ├── Constantes.java
    ├── FIFOTransport.java
    ├── Message.java
    └── MyRandom.java

5 directories, 12 files
```
## building
```bash
make all
```
You will have now a `run.sh` file. Just execute it.
```bash
./run config/nh.txt
```

## Config files
+ étude experimental 1 -> `nh.txt`

## to-do
- [x] makefile
- [ ] verify the critical section


## notes 
