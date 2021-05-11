package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{IDF => fromSparkML}
class IDFTest extends TestBase {
  "IDF build success" should "build new IDF from given parameters and return the same args as SparkML IDF" in {
    val CaraDsFeature=IDF(
      Map (
        "InputCol"->"Input",
        "OutputCol" -> "Col10"
        ,"MinDocFreq" -> "4"
      ))
    val SparkFeature=new fromSparkML()
      .setInputCol("Input")
      .setOutputCol("Col10")
      .setMinDocFreq(4)
    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList

    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList
    CaraDsParams should contain theSameElementsAs  SparkParams
  }
}
