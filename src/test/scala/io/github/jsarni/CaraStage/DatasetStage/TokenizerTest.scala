package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{Tokenizer => fromSparkML}

class TokenizerTest extends TestBase {

  "Tokenizer build Success" should "build new Tokenizer from given parameters and return the same args as SparkML Tokenizer" in {
    val CaraDsFeature = Tokenizer(Map("InputCol" -> "Input", "OutputCol" -> "Col10"))

    val SparkFeature = new fromSparkML()
      .setInputCol("Input")
      .setOutputCol("Col10")

    val CaraDsParams = CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs SparkParams
  }

}
