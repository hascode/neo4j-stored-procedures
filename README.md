# Neo4j Stored Procedures Tutorial

A simple demonstration of extending [Neo4j]'s capabilities by writing custom 
stored procedures.

## Running the Tests with Maven

The tests may be run with [Maven] using the following command:

```bash
mvn test
```



## Running with Docker

Use the following command to build the stored procedures and run them with Docker:

```bash
mvn clean package -Dmaven.test.skip=true && \
mkdir target/plugintmp && \      
cp target/neo4j-stored-procedures-1.0.0.jar target/plugintmp && \
docker run -td --rm -v $PWD/target/plugintmp:/plugins -p 7474:7474 -p 7687:7687 neo4j:3.3.2
```

Please feel free to read the detailed tutorial on [my blog]: "[Implementing, Testing and Running Stored Procedures for Neo4j](http://www.hascode.com/2018/02/implementing-testing-and-running-stored-procedures-for-neo4j)".

---

   [my blog]:http://www.hascode.com/
   [Maven]:http://maven.apache.org/
   [Neo4j]:https://neo4j.com/

**2018 Micha Kops / hasCode.com**