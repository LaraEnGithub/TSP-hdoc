# TSP-hyoc
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

For packing without running unit tests:

```bash
mvn package -DskipTests
```

#### Available flags

Solving exhaustively using permutations:
```bash
java -jar target/tsp.jar -p <path/sql-file.sql> <path/tsp-file.tsp>
```

Saving the results:
```bash
java -jar target/tsp.jar -s <path/sql-file.sql> <path/tsp-file.tsp>
```
_Note: flags can be combined_

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
