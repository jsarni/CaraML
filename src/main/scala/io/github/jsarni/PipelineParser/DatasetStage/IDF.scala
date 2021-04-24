package io.github.jsarni.PipelineParser.DatasetStage
import org.apache.spark.ml.feature.{IDF => Idf}

case class IDF(inputCol:String,outputCol: String) extends DatasetStage {
  val Idf= new Idf()
    .setInputCol(inputCol)
    .setOutputCol(outputCol)
}
