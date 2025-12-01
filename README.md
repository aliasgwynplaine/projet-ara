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
mkdir build
javac -cp "your lib jars" your-sources-here -d build
jar cvf output.jar build/*
java -cp "yourlibs:output.jar" peersim.Simulator path2config
```

## to-do
- [ ] makefile


## notes 
