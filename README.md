# Neo4j Stored Procedures Tutorial

TODO..

## Running with Docker

Use the following command to build the stored procedures and run them with Docker:

```bash
mvn clean package -Dmaven.test.skip=true && \
cd target && mkdir plugintmp && \
cp neo4j-stored-procedures-1.0.0.jar plugintmp && \
docker run -td --rm -v $PWD/plugintmp:/plugins -p 7474:7474 -p 7687:7687 neo4j:3.3.2
```

Please feel free to read the detailed tutorial on [my blog] at: XXXX

---

   [my blog]:http://www.hascode.com/

**2018 Micha Kops / hasCode.com**