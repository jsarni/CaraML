package io.github.jsarni.carastage.datasetstage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{HashingTF => fromSparkML}

class HashingTFTest extends TestBase {

  "HashingTF build Success" should "build new HashingTF from given parameters and return the same args as SparkML HashingTF" in {

    val CaraDsFeature=HashingTF(
      Map(
        "Binary" -> "true",
        "InputCol" -> "Input",
        "OutputCol" -> "Col10",
        "NumFeatures" -> "2"
      )
    )
    val SparkFeature=new fromSparkML()
      .setBinary(true)
      .setInputCol("Input")
      .setNumFeatures(2)
      .setOutputCol("Col10")

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "HashingTF build Failure" should "fail to build HashingTF with wrong parameters" in {
    an [IllegalArgumentException] must be thrownBy HashingTF(
      Map(
        "Binary" -> "OK",
        "InputCol" -> "Input",
        "OutputCol" -> "Col10",
        "NumFeatures" -> "2"
      )
    ).build().get

    an [IllegalArgumentException] must be thrownBy HashingTF(
      Map(
        "Binary" -> "OK",
        "InputCol" -> "Input",
        "OutputCol" -> "Col10",
        "NumFeatures" -> "0"
      )
    ).build().get
  }

}
