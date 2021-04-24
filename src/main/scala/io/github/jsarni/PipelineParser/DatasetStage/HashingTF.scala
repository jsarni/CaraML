package io.github.jsarni.PipelineParser.DatasetStage
import org.apache.spark.ml.feature.{HashingTF=> Hashingtf}
case class HashingTF(inputCol:String,outputCol: String) extends DatasetStage {
  val hashingTF= new Hashingtf()
    .setInputCol(inputCol)
    .setOutputCol(outputCol)
}
