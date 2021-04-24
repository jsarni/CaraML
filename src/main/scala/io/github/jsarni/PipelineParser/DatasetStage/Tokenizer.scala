package io.github.jsarni.PipelineParser.DatasetStage
import org.apache.spark.ml.feature.{Tokenizer=>Token}
case class Tokenizer(inputCol:String,outputCol: String) extends DatasetStage {
  val tokenizer= new Token()
                    .setInputCol(inputCol)
                    .setOutputCol(outputCol)
}
