package models

import org.w3.banana.SparqlOpsModule
import org.w3.banana.jena.JenaModule
import org.w3.banana.jena.Jena
import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.tdb.TDBFactory
import play.api.Logger

import org.w3.banana.io.RDFWriter
import org.w3.banana.io.JsonLdExpanded
import org.w3.banana.io.JsonLdFlattened

trait SPARQLDatabaseJena extends SPARQLDatabaseTrait[Jena, Dataset] with JenaModule {
  type DATASET = Dataset
  override lazy val dataset: DATASET = {
    val dataset = TDBFactory.createDataset("TDB")
    Logger.info(s"RDFStoreLocalJena1Provider dataset created $dataset")
    dataset
  }
  
  implicit val jsonldExpandedWriter:
  RDFWriter[Rdf, scala.util.Try, JsonLdExpanded] =
    jsonldCompactedWriter.asInstanceOf[
      RDFWriter[Rdf, scala.util.Try, JsonLdExpanded]]
  implicit val jsonldFlattenedWriter:
  RDFWriter[Rdf, scala.util.Try, JsonLdFlattened] =
    jsonldCompactedWriter.asInstanceOf[
        RDFWriter[Rdf, scala.util.Try, JsonLdFlattened] ]
}