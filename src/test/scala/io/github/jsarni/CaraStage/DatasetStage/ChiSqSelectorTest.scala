package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{ChiSqSelector => fromSparkML}

import java.lang.reflect.InvocationTargetException

class ChiSqSelectorTest extends TestBase {
  "ChiSqSelector build Success" should
    "build new ChiSqSelector from given parameters, and must be the same with SparkML ChiSqSelector" in {

    val CaraDsFeature=ChiSqSelector(
      Map(
        "Fdr" -> "0.01",
        "FeaturesCol" -> "Input",
        "Fpr" -> "0.2",
        "Fwe" -> "0.2",
        "LabelCol" -> "col1",
        "OutputCol" -> "Output",
        "NumTopFeatures" -> "40",
        "Percentile" -> "0.1",
        "SelectorType"-> "fpr"
      )
    )

    val SparkFeature=new fromSparkML()
      .setFdr(0.01)
      .setFeaturesCol("Input")
      .setFpr(0.2)
      .setFwe(0.2)
      .setLabelCol("col1")
      .setOutputCol("Output")
      .setNumTopFeatures(40)
      .setPercentile(0.1)
      .setSelectorType("fpr")

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "ChiSqSelector build Failure" should "fail to build new ChiSqSelector with wrong parameters" in {

    an [InvocationTargetException] must be thrownBy ChiSqSelector(
      Map(
        "Fdr" -> "10.0",
        "FeaturesCol" -> "Input",
        "Fpr" -> "0.2",
        "Fwe" -> "0.2",
        "LabelCol" -> "col1",
        "OutputCol" -> "Output",
        "NumTopFeatures" -> "40",
        "Percentile" -> "0.1",
        "SelectorType"-> "fpr"
      )
    ).build().get
  }

  an [InvocationTargetException] must be thrownBy ChiSqSelector(
    Map(
      "Fdr" -> "1.0",
      "FeaturesCol" -> "Input",
      "Fpr" -> "20.0",
      "Fwe" -> "0.2",
      "LabelCol" -> "col1",
      "OutputCol" -> "Output",
      "NumTopFeatures" -> "40",
      "Percentile" -> "0.1",
      "SelectorType"-> "fpr"
    )
  ).build().get

  an [InvocationTargetException] must be thrownBy ChiSqSelector(
    Map(
      "Fdr" -> "1.0",
      "FeaturesCol" -> "Input",
      "Fpr" -> "0.2",
      "Fwe" -> "20.2",
      "LabelCol" -> "col1",
      "OutputCol" -> "Output",
      "NumTopFeatures" -> "40",
      "Percentile" -> "0.1",
      "SelectorType"-> "fpr"
    )
  ).build().get

  an [InvocationTargetException] must be thrownBy ChiSqSelector(
    Map(
      "Fdr" -> "1.0",
      "FeaturesCol" -> "Input",
      "Fpr" -> "0.2",
      "Fwe" -> "0.2",
      "LabelCol" -> "col1",
      "OutputCol" -> "Output",
      "NumTopFeatures" -> "40",
      "Percentile" -> "10.1",
      "SelectorType"-> "fpr"
    )
  ).build().get
}
