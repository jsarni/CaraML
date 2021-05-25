package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.classification.{LogisticRegression => SparkLR}


class LogisticRegressionTest extends TestBase {

  "build" should "Create an lr model and set all parameters with there args values or set default ones" in {
    val params = Map(
      "MaxIter" -> "10",
      "RegParam" -> "0.3",
      "ElasticNetParam" -> "0.1",
      "Family" -> "multinomial",
      "FeaturesCol" -> "FeatureColname",
      "FitIntercept" -> "True",
      "PredictionCol" -> "Age",
      "ProbabilityCol" -> "ProbaColname",
      "RawPredictionCol"-> "RawPredictColname",
      "Standardization" -> "True",
      "Tol" -> "0.13",
      "WeightCol" -> "WeightColname"
    )
    val lr = LogisticRegression(params)
    val lrWithTwoParams = new SparkLR()
      .setRegParam(0.8)
      .setStandardization(false)

    val expectedResult = List(
      new SparkLR()
        .setMaxIter(10)
        .setRegParam(0.3)
        .setElasticNetParam(0.1)
        .setFamily("multinomial")
        .setFeaturesCol("FeatureColname")
        .setFitIntercept(true)
        .setPredictionCol("Age")
        .setProbabilityCol("ProbaColname")
        .setRawPredictionCol("RawPredictColname")
        .setStandardization(true).setTol(0.13)
        .setWeightCol("WeightColname")
    )
    lr.build().isSuccess shouldBe true

    val res = List(lr.build().get)
    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head

//    Test default values of unset params
    lrWithTwoParams.getMaxIter shouldBe 100
    lrWithTwoParams.getFamily shouldBe "auto"
    lrWithTwoParams.getTol shouldBe 0.000001

  }
  "GetMethode" should "Return the appropriate methode by it's name" in {
    val params = Map(
      "MaxIter" -> "10",
      "RegParam" -> "0.3",
      "ElasticNetParam" -> "0.1",
      "Family" -> "multinomial",
      "FeaturesCol" -> "FeatureColname",
      "FitIntercept" -> "True",
      "PredictionCol" -> "Age",
      "ProbabilityCol" -> "ProbaColname",
      "RawPredictionCol"-> "RawPredictColname",
      "Standardization" -> "True",
      "Tol" -> "0.13",
      "WeightCol" -> "WeightColname"
    )
    val caraLr = LogisticRegression(params)
    val model =caraLr.build().get.asInstanceOf[SparkLR]

    caraLr.getMethode(model,10,"MaxIter").getName shouldBe "setMaxIter"
    caraLr.getMethode(model,0.0,"RegParam").getName shouldBe "setRegParam"
    caraLr.getMethode(model, false ,"Standardization").getName shouldBe "setStandardization"

  }
}

