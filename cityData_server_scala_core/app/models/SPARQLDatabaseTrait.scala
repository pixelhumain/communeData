package models

import org.w3.banana.SparqlOpsModule
import org.w3.banana.RDF
import org.w3.banana.JsonLDWriterModule

trait SPARQLDatabaseTrait[Rdf <: RDF, DATASET]
extends RDFStoreLocalProvider[Rdf, DATASET]
with JsonLDWriterModule {
  import ops._
  import rdfStore.transactorSyntax._
  import rdfStore.graphStoreSyntax._
  import sparqlOps._
  import rdfStore.sparqlEngineSyntax._

  def runQuery(queryString: String): String = {
        val query =parseConstruct(queryString)

    val res = dataset.r({
      val result = for {
        query <- parseConstruct(queryString)
        graph <- dataset.executeConstruct(query, Map())
      } yield graph
      result
    })
    val graph = res.get.get
    jsonldCompactedWriter.asString(graph, "" ).get
  }
}