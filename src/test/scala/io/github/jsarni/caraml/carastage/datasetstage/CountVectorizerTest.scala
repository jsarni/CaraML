package io.github.jsarni.caraml.carastage.datasetstage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{CountVectorizer => fromSparkML}

class CountVectorizerTest extends TestBase {
  "CountVectorizer build Success" should
    "build new CountVectorizer from given parameters and return the same args as SparkML CountVectorizer" in {

    val CaraDsFeature = CountVectorizer(
      Map(
        "Binary" -> "true",
        "InputCol" -> "Input",
        "MaxDF" -> "15.0",
        "MinDF" -> "8.0",
        "OutputCol" -> "Col10",
        "MinTF" -> "10.0",
        "VocabSize" -> "4"
      )
    )

    val SparkFeature = new fromSparkML()
      .setBinary(true)
      .setInputCol("Input")
      .setMaxDF(15.0)
      .setMinDF(8.0)
      .setMinTF(10.0)
      .setOutputCol("Col10")
      .setVocabSize(4)

    val CaraDsParams = CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "CountVectorizer build Failure" should "fail to build CountVectorizer with wrong parameters" in {

    an [IllegalArgumentException] must be thrownBy CountVectorizer(
      Map(
        "Binary" -> "OK",
        "InputCol"->"Input",
        "MaxDF"->"15.0",
        "MinDF" ->"8.0",
        "OutputCol" -> "Col10",
        "MinTF" -> "10.0",
        "VocabSize" -> "4"
      )
    ).build().get

  }
}
