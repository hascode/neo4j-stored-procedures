package it;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hascode.neo4j.ArchitectureMetrics;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.harness.junit.Neo4jRule;

public class ArchitectureMetricsTest {

  @Rule
  public Neo4jRule neo4j = new Neo4jRule().withProcedure(ArchitectureMetrics.class);

  @Test
  public void mustCalculatePackageAbstractness() throws Exception {
    try (Driver driver = GraphDatabase
        .driver(neo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {
      Session session = driver.session();
      session.run(
          "CREATE (c1:Type:Class{name:'Foo', fqn:'com.hascode.Foo', abstract:true}),"
              + "(c2:Type:Class{name:'Bar', fqn:'com.hascode.Bar'}),"
              + "(c3:type:Interface{name:'IBaz', fqn:'com.hascode.Baz'})"
              + "RETURN c1,c2,c3");

      StatementResult result = session
          .run("CALL hascode.abstractnessForPackage('com.hascode')");

      final Double abstractness = result.single().get("abstractness").asDouble();
      assertThat("abstractness of package com.hascode should be 0.5", abstractness, equalTo(0.5));
    }
  }

}
