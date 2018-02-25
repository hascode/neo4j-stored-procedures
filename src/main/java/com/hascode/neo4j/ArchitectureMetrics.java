package com.hascode.neo4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

public class ArchitectureMetrics {

  private static final String ABSTRACTNESS_QUERY = "MATCH (t:Type)\n"
      + "WHERE t.fqn STARTS WITH {packageFqn}\n"
      + "AND (t.abstract IS NULL\n"
      + "OR NOT t:Interface)\n"
      + "WITH COUNT(t) AS nonabstract\n"
      + "MATCH (t:Type)\n"
      + "WHERE t.fqn STARTS WITH {packageFqn}\n"
      + "AND (t.abstract = TRUE\n"
      + "OR t:Interface)\n"
      + "WITH nonabstract, COUNT(t) AS abstract\n"
      + "RETURN toFloat(abstract) / toFloat(nonabstract) AS abstractness";

  public static class Abstractness {

    public final double abstractness;

    public Abstractness(double abstractness) {
      this.abstractness = abstractness;
    }
  }

  @Context
  public GraphDatabaseService db;

  @Context
  public Log log;

  @Procedure(name = "hascode.abstractnessForPackage", mode = Mode.READ)
  public Stream<Abstractness> abstractnessForPackage(@Name("packageFqn") String packageFqn) {
    Objects.requireNonNull(packageFqn, "packageFqn must not be null");

    log.info("call hascode.abstractnessForPackage for package: `%s`", packageFqn);

    Map<String, Object> params = new HashMap<>();
    params.put("packageFqn", packageFqn);

    Double abstractness = -1D; // cheesy fallback
    try (Transaction tx = db.beginTx(); Result result = db.execute(ABSTRACTNESS_QUERY, params)) {
      ResourceIterator<Object> resourceIterator = result.columnAs("abstractness");
      if (resourceIterator.hasNext()) {
        abstractness = (Double) resourceIterator.next();
      }
      tx.success();
    }
    return Stream.of(new Abstractness(abstractness));
  }


}
