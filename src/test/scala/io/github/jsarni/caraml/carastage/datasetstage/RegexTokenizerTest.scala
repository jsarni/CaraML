package io.github.jsarni.caraml.carastage.datasetstage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{RegexTokenizer => fromSparkML}

import java.lang.reflect.InvocationTargetException

class RegexTokenizerTest extends TestBase {
  "RegexTokenizer build Success" should
    "build new RegexTokenizer from given parameters and return the same args as SparkML RegexTokenizer" in {

    val CaraDsFeature=RegexTokenizer(
      Map(
        "Gaps" -> "true",
        "InputCol" -> "Input",
        "MinTokenLength" -> "1",
        "Pattern" -> " * ",
        "OutputCol" -> "Col10",
        "ToLowercase" -> "false"
      )
    )

    val SparkFeature=new fromSparkML()
      .setGaps(true)
      .setInputCol("Input")
      .setMinTokenLength(1)
      .setPattern(" * ")
      .setToLowercase(false)
      .setOutputCol("Col10")

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "RegexTokenizer build Failure" should "fail to build RegexTokenizer with wrong parameters" in {

    an [IllegalArgumentException] must be thrownBy RegexTokenizer(
      Map(
        "Gaps" -> "12",
        "InputCol" -> "Input",
        "MinTokenLength" -> "1",
        "Pattern" -> " * ",
        "OutputCol" -> "Col10",
        "ToLowercase" -> "false"
      )).build().get

    an [InvocationTargetException] must be thrownBy RegexTokenizer(
      Map(
        "Gaps" -> "true",
        "InputCol" -> "Input",
        "MinTokenLength" -> "-1",
        "Pattern" -> " * ",
        "OutputCol" -> "Col10",
        "ToLowercase" -> "false"
      )).build().get
  }

}
