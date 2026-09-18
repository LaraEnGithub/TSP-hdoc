# TSP-hdoc
---
(Soon to be completed) Implementation of Simulated Annealing for solving a variant of the Traveler Salesman Problem.
---

### Stack

This project is written for **Java 21**. To compile the project, **Maven 3.8.7** is used. To run unit tests, **JUnit 5**. To read data, SQLite is used (via `sqlite-jdbc`).

### Compiling

```bash
mvn compile
```


### Testing

```bash
mvn test
```

Without showing standard output:

```bash
mvn -q test
```


### Packaging and running the program

```bash
mvn package
java -jar target/tsp.jar <path/sql-file.sql> <path/tsp-file.tsp>
```

With no flags, it prints the file-order route, the maximum calculated distance, normalizer value, evaluated cost and feasibility.

For packing without running unit tests:

```bash
mvn package -DskipTests
```

#### Available flags

```
tsp [-p | -a | -s <runs>] [-v] [-t <threads>] <first path> <path/tsp-file.tsp>
```

`-p`, `-a` and `-s` are mutually exclusive modes. `<first path>` is a `.sql` dump for the default mode and for `-p`; it is a `.properties` configuration file (see below) for `-a` and `-s`.

Solving exhaustively using permutations: needs a `.sql` dump. Refuses instances of 12 cities or more:
```bash
java -jar target/tsp.jar -p <path/sql-file.sql> <path/tsp-file.tsp>
```

Running the annealing heuristic once: needs a `.properties` configuration:
```bash
java -jar target/tsp.jar -a <path/config.properties> <path/tsp-file.tsp>
```

Add `-v` to also print a live trace (attempts, current cost, best cost, temperature) to standard error as the run progresses:
```bash
java -jar target/tsp.jar -a -v <path/config.properties> <path/tsp-file.tsp>
```

Sweeping `<runs>` independent seeds and reporting the best
```bash
java -jar target/tsp.jar -s 100 <path/config.properties> <path/tsp-file.tsp>
```

Add `-t <threads>` to run the sweep across that many threads 
```bash
java -jar target/tsp.jar -s 100 -t 4 <path/config.properties> <path/tsp-file.tsp>
```

`-v` only has an effect together with `-a`; `-t` only has an effect together with `-s`. 

#### Configuration file

`-a` and `-s` read their parameters from a `.properties` file:  see [config/annealing.properties](config/annealing.properties) for a working example. Requires: `sql` (path to the `.sql` dump), `seed`, `initialTemperature`, `coolingRate`, `batchSize`, `epsilon`, `maxBatchAttempts`, `totalAttempts`, `targetAcceptance`, `searchTemperature` (`true`/`false`).

### Limpiar
To delete target:

```bash
mvn clean
```

For a clean build:

```bash
mvn clean package
```

<!--
 _
//\
V  \
 \  \_
  \,'.`-.
   |\ `. `.       
   ( \  `. `-.                        _,.-:\
    \ \   `.  `-._             __..--' ,-';/
     \ `.   `-.   `-..___..---'   _.--' ,'/
      `. `.    `-._        __..--'    ,' /
        `. `-_     ``--..''       _.-' ,'
          `-_ `-.___        __,--'   ,'
             `-.__  `----"""    __.-'
                  `--..____..--'
 -->
