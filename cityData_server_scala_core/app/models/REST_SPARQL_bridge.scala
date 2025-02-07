package models

import play.api.mvc.Request
import play.api.mvc.AnyContent
import org.w3.banana.RDF

trait REST_SPARQL_bridge[Rdf <: RDF, DATASET] extends SPARQLDatabaseTrait[Rdf, DATASET] {

  val dbo = "http://dbpedia.org/ontology/"
  val dbp = "http://dbpedia.org/resource/"

  val queryBeginning = """
    PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>
    PREFIX dbpedia:     <http://dbpedia.org/resource/>
    CONSTRUCT {
      ?CITY ?P ?O .
    }
    WHERE {
      ?CITY a dbpedia-owl:Settlement ;
    """
  val queryEnding = """
              ?P ?O .
    }
    """

  /**
   * For URI's like
   *   /baseURL/cities/fra/01600
   *
   * create SPARQL query: <pre>
    
     PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>
     PREFIX dbpedia:     <http://dbpedia.org/resource/>
     CONSTRUCT {
       ?CITY ?P ?O .
     }
     WHERE {
       ?CITY a dbpedia-owl:Settlement ;
             dbpedia-owl:country dbpedia:France ;
             dbpedia-owl:postalCode 01600 ;
             ?P ?O .
     }
   </pre>
   */
  def getJSONLD(request: Request[AnyContent]): String = {
    println("getJSONLD: " + request)
    val conf = RequestRDFConfig(Seq(
      new RequestRDFPathName(dbo + "country") {
        val iso3166ToDBPedia = Map(
          "fra" -> (dbp + "France"))
        override def map(value: String): String = wrapAsURI(iso3166ToDBPedia.get(value).get)
      },
      RequestRDFPathName(dbo + "postalCode")))
    runQuery(makeSPARQL(request, conf))
  }

  private def makeSPARQL(request: Request[AnyContent], conf: RequestRDFConfig): String = {
    val path = request.path
    val elems = path.split("/").drop(2)
    // elems contains e.g.  "fr", "01600"
    val propsAndValues = conf.mappings.zip(elems)
    val criteria: Seq[String] = propsAndValues.map {
      x =>
        wrapAsURI(x._1.uri) + " " + x._1.map(x._2) + " ;\n"
    }
    val queryString = queryBeginning + criteria.mkString + queryEnding
    println("makeSPARQL: " + queryString)
    queryString
  }

  def wrapAsURI(uri: String) = s"<$uri>"
  def wrapAsString(string: String) = s""""${string}""""

  case class RequestRDFPathName(uri: String) {
    def map(value: String): String = wrapAsString(value)
  }
  case class RequestRDFConfig(mappings: Seq[RequestRDFPathName])

}