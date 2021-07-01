package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkML}
import io.github.jsarni.TestBase

class DecisionTreeRegressorTest extends TestBase {

  "build" should "Create an lr model and set all parameters with there args values or set default ones" in {
    val params = Map(
      "CheckpointInterval" -> "10",
      "FeaturesCol" -> "FeatureCol",
      "Impurity" -> "variance",
      "LabelCol" -> "LabelCol",
      "LeafCol" -> "LeafCol",
      "MaxBins" -> "10",
      "MaxDepth" -> "5",
      "MinInfoGain"-> "0.02",
      "MinInstancesPerNode" -> "2",
      "MinWeightFractionPerNode" -> "0.03",
      "PredictionCol" -> "PredictionCol",
      "Seed"  -> "124555",
      "VarianceCol" -> "VarCol",
      "WeightCol"  -> "1.2"
    )
    val dTree = DecisionTreeRegressor(params)
    val dTreeWithTwoParams = new SparkML()
      .setCheckpointInterval(5)
      .setMaxDepth(10)

    val expectedResult = List(
      new SparkML()
        .setCheckpointInterval(10)
        .setFeaturesCol("FeatureCol")
        .setImpurity("variance")
        .setLabelCol("LabelCol")
        .setLeafCol("LeafCol")
        .setMaxBins(10)
        .setMaxDepth(5)
        .setMinInfoGain(0.02)
        .setMinInstancesPerNode(2)
        .setMinWeightFractionPerNode(0.03)
        .setPredictionCol("PredictionCol")
        .setSeed(124555.toLong)
        .setVarianceCol("VarCol")
        .setWeightCol("1.2")

    )
    dTree.build().isSuccess shouldBe true

    val res = List(dTree.build().get)
    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    val resContain = resParameters(0).toList
    val expectedContain = expectedParameters(0).toList

    resContain should contain theSameElementsAs expectedContain

    dTreeWithTwoParams.getImpurity shouldBe "variance"
    dTreeWithTwoParams.getMaxBins shouldBe 32
    dTreeWithTwoParams.getMinInfoGain shouldBe 0.0
  }

  "GetMethode" should "Return the appropriate methode by it's name" in {
    val params = Map(
      "CheckpointInterval" -> "10",
      "FeaturesCol" -> "FeatureCol",
      "Impurity" -> "variance",
      "LabelCol" -> "LabelCol",
      "LeafCol" -> "LeafCol",
      "MaxBins" -> "10",
      "MaxDepth" -> "5",
      "MinInfoGain"-> "0.02",
      "MinInstancesPerNode" -> "2",
      "MinWeightFractionPerNode" -> "0.03",
      "PredictionCol" -> "PredictionCol",
      "Seed"  -> "124555",
      "VarianceCol" -> "VarCol",
      "WeightCol"  -> "1.2"
    )
    val caraLr = DecisionTreeRegressor(params)
    val model =caraLr.build().get.asInstanceOf[SparkML]

    caraLr.getMethode(model,10.toLong,"Seed").getName shouldBe "setSeed"
    caraLr.getMethode(model,"PredictCol","PredictionCol").getName shouldBe "setPredictionCol"
    caraLr.getMethode(model, 10 ,"CheckpointInterval").getName shouldBe "setCheckpointInterval"
  }

}

