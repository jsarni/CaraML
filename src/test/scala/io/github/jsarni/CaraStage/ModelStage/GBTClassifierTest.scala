package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.classification.{GBTClassifier => SparkML}

class GBTClassifierTest extends TestBase {
  "build" should "Create an lr model and set all parameters with there args values or set default ones" in {
    val params = Map(
      "CheckpointInterval" -> "10",
      "FeaturesCol" -> "FeatureCol",
      "LabelCol" -> "LabelCol",
      "FeaturesCol" -> "FeatureColname",
      "LeafCol" -> "LeafCol",
      "MaxBins" -> "10",
      "MaxDepth" -> "5",
      "MinInfoGain"-> "0.02",
      "MinInstancesPerNode" -> "2",
      "MinWeightFractionPerNode" -> "0.03",
      "PredictionCol" -> "PredictionCol",
      "ProbabilityCol" -> "ProbabilityCol",
      "RawPredictionCol" -> "RawPredictionCol",
      "Seed"  -> "124555",
      "Thresholds" -> "0.2, 0.04",
      "WeightCol"  -> "1.2",
      "FeatureSubsetStrategy" -> "auto" ,
      "SubsamplingRate" -> "0.5",
      "LossType" -> "logistic",
      "MaxIter" -> "20",
      "StepSize" -> "0.3" ,
      "ValidationIndicatorCol" -> "ValidationIndicatorCol"
    )
    val gbt = GBTClassifier(params)
    val gbtWithTwoParams = new SparkML()
      .setCheckpointInterval(5)
      .setMaxDepth(10)

    val expectedResult = List(
      new SparkML()
        .setCheckpointInterval(10)
        .setFeaturesCol("FeatureCol")
        .setLabelCol("LabelCol")
        .setFeaturesCol("FeatureColname")
        .setLeafCol("LeafCol")
        .setMaxBins(10)
        .setMaxDepth(5)
        .setMinInfoGain(0.02)
        .setMinInstancesPerNode(2)
        .setMinWeightFractionPerNode(0.03)
        .setPredictionCol("PredictionCol")
        .setProbabilityCol("ProbabilityCol")
        .setRawPredictionCol("RawPredictionCol")
        .setSeed(124555.toLong)
        .setWeightCol("1.2")
        .setThresholds(Array(0.2, 0.04))
        .setFeatureSubsetStrategy("auto")
        .setSubsamplingRate(0.5)
        .setLossType("logistic")
        .setMaxIter(20)
        .setStepSize(0.3)
        .setValidationIndicatorCol("ValidationIndicatorCol")
    )
    gbt.build().isSuccess shouldBe true

    val res = List(gbt.build().get)
    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head

    gbtWithTwoParams.getMinInstancesPerNode shouldBe 1
    gbtWithTwoParams.getLossType shouldBe "logistic"
    gbtWithTwoParams.getStepSize shouldBe 0.1
  }

  "GetMethode" should "Return the appropriate methode by it's name" in {
    val params = Map(
      "CheckpointInterval" -> "10",
      "FeaturesCol" -> "FeatureCol",
      "LabelCol" -> "LabelCol",
      "FeaturesCol" -> "FeatureColname",
      "LeafCol" -> "LeafCol",
      "MaxBins" -> "10",
      "MaxDepth" -> "5",
      "MinInfoGain"-> "0.02",
      "MinInstancesPerNode" -> "2",
      "MinWeightFractionPerNode" -> "0.03",
      "PredictionCol" -> "PredictionCol",
      "ProbabilityCol" -> "ProbabilityCol",
      "RawPredictionCol" -> "RawPredictionCol",
      "Seed"  -> "124555",
      "Thresholds" -> "0.2, 0.04",
      "WeightCol"  -> "1.2",
      "LossType" -> "logistic",
      "MaxIter" -> "20",
      "StepSize" -> "0.3" ,
      "ValidationIndicatorCol" -> "ValidationIndicatorCol"
    )
    val caraLr = GBTClassifier(params)
    val model =caraLr.build().get.asInstanceOf[SparkML]

    caraLr.getMethode(model,10.toLong,"Seed").getName shouldBe "setSeed"
    caraLr.getMethode(model,"PredictCol","PredictionCol").getName shouldBe "setPredictionCol"
    caraLr.getMethode(model, 10 ,"CheckpointInterval").getName shouldBe "setCheckpointInterval"
  }

}

