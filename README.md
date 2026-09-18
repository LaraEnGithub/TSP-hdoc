# TSP-hdoc
---
Implementation of Threshold Accepting, a Simulated Annealing variant, for solving a variant of the Traveler Salesman Problem.
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

With no flags, it prints the number of cities, the maximum calculated distance, feasibility, the evaluated cost and the file-order route.

For packing without running unit tests:

```bash
mvn package -DskipTests
```

#### Available flags

```
tsp [-p | -a | -s <runs>] [-v] [-t <threads>] [-w <walk.csv>] <first path> <path/tsp-file.tsp>
```

`-p`, `-a` and `-s` are mutually exclusive modes. `<first path>` is a `.sql` dump for the default mode and for `-p`; it is a `.properties` configuration file (see below) for `-a` and `-s`.

Solving exhaustively using permutations: needs a `.sql` dump. Refuses instances of 12 cities or more:
```bash
java -jar target/tsp.jar -p <path/sql-file.sql> <path/tsp-file.tsp>
```

Running the annealing heuristic once: needs a `.properties` configuration. Also prints the stop reason, how many batches `maxBatchAttempts` cut short, and the acceptance rate:
```bash
java -jar target/tsp.jar -a <path/config.properties> <path/tsp-file.tsp>
```

Add `-v` to print `E:<cost>` to standard error on every new best:
```bash
java -jar target/tsp.jar -a -v <path/config.properties> <path/tsp-file.tsp>
```

Add `-w` to write the accepted-solution walk as `evaluations,temperature,cost`, one row every 500 accepted moves:
```bash
java -jar target/tsp.jar -a -w results/walk.csv <path/config.properties> <path/tsp-file.tsp>
```

Sweeping `<runs>` independent seeds. Writes a `seed,cost` CSV to standard output and the progress to standard error:
```bash
java -jar target/tsp.jar -s 100 <path/config.properties> <path/tsp-file.tsp> > results/seeds.csv
```

On PowerShell use `| Out-File -Encoding utf8` instead of `>`, which writes UTF-16.

Add `-t <threads>` to run the sweep across that many threads 
```bash
java -jar target/tsp.jar -s 100 -t 4 <path/config.properties> <path/tsp-file.tsp>
```

`-v` and `-w` only have an effect together with `-a`; `-t` only has an effect together with `-s`. 

#### Configuration file

`-a` and `-s` read their parameters from a `.properties` file:  see [config/annealing.properties](config/annealing.properties) for a working example. Requires: `sql` (path to the `.sql` dump), `seed`, `initialTemperature`, `coolingRate`, `batchSize`, `epsilon`, `maxBatchAttempts`, `totalAttempts`, `targetAcceptance`, `searchTemperature` (`true`/`false`).

### Analysis

Code goes in [analysis/](analysis) and is versioned; anything the program writes goes in `results/`, which is not. Run the scripts from the project root.

```bash
pip install -r analysis/requirements.txt
```

Plotting the walk written by `-w`:
```bash
python analysis/plot.py results/walk.csv
```

Feasibility rate, cost statistics and a histogram over the sweeps written by `-s`. With no arguments it reports every `.csv` under `results/`:
```bash
python analysis/feasible.py results/seeds.csv
```

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
