package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.classification.{NaiveBayes => SparkML}
import io.github.jsarni.TestBase

class NaiveBayesTest extends TestBase {
  "build" should "Create an lr model and set all parameters with there args values or set default ones" in {
    val params = Map(
      "FeaturesCol" -> "FeaturesCol",
      "LabelCol" -> "LabelCol",
      "ModelType" -> "gaussian",
      "PredictionCol" -> "PredictionCol",
      "ProbabilityCol" -> "ProbabilityCol",
      "RawPredictionCol" -> "RawPredictionCol",
      "Smoothing" -> "0.8",
      "Thresholds" -> "0.2, 0.4, 1.05",
      "WeightCol" -> "WeightCol"
    )
    val NBayes = NaiveBayes(params)
    val NBayesWithTwoParams = new SparkML()
      .setFeaturesCol("FeaturesCol")
      .setThresholds(Array(0.5, 1.44))

    val expectedResult = List(
      new SparkML()
        .setFeaturesCol("FeaturesCol")
        .setLabelCol("LabelCol")
        .setModelType("gaussian")
        .setPredictionCol("PredictionCol")
        .setProbabilityCol("ProbabilityCol")
        .setRawPredictionCol("RawPredictionCol")
        .setSmoothing(0.8)
        .setThresholds(Array(0.2, 0.4, 1.05))
        .setWeightCol("WeightCol")
    )
    NBayes.build().isSuccess shouldBe true

    val res = List(NBayes.build().get)
    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head

    NBayesWithTwoParams.getSmoothing shouldBe 1.0
    NBayesWithTwoParams.getModelType shouldBe "multinomial"
  }

  "GetMethode" should "Return the appropriate methode by it's name" in {
    val params = Map(
      "FeaturesCol" -> "FeaturesCol",
      "LabelCol" -> "LabelCol",
      "ModelType" -> "gaussian",
      "PredictionCol" -> "PredictionCol",
      "ProbabilityCol" -> "ProbabilityCol",
      "RawPredictionCol" -> "RawPredictionCol",
      "Smoothing" -> "0.8",
      "Thresholds" -> "0.2, 0.4, .05",
      "WeightCol" -> "WeightCol"
    )
    val caraNaivebayes = NaiveBayes(params)
    val model =caraNaivebayes.build().get.asInstanceOf[SparkML]

    caraNaivebayes.getMethode(model,"String","FeaturesCol").getName shouldBe "setFeaturesCol"
    caraNaivebayes.getMethode(model,0.0,"Smoothing").getName shouldBe "setSmoothing"
    caraNaivebayes.getMethode(model, Array(1.0,0.2) ,"Thresholds").getName shouldBe "setThresholds"
  }
}