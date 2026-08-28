# TSP-hyoc
---
(Próxima) Implementación del recocido simulado para resolver instancias del Probelma del Agente Viajero
---

El proyecto está escrito para **Java 21**. Para compilar el proyecto se usa **Maven 3.8.7**. Para ejecutar los *tests* se usa **JUnit 5**.

### Compilar

```bash
mvn compile
```


### Ejecutar tests

```bash
mvn test
```

Sin mostrar salida estándar:

```bash
mvn -q test
```


### Empaquetar y ejecutar programa

```bash
mvn package
java -jar target/tsp-1.0-SNAPSHOT.jar

```
Para empaquetar sin correr pruebas:

```bash
mvn package -DskipTests
```


### Limpiar
Para borrar el target:

```bash
mvn clean
```

Para un build limpio:

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
