package io.github.jsarni.CaraStage.ModelStage

import org.apache.spark.ml.clustering.{LDA => SparkML}
import io.github.jsarni.TestBase

class LDATest extends TestBase {
  "build" should "Create an lr model and set all parameters with there args values or set default ones" in {
    val params = Map(
      "CheckpointInterval" -> "3",
      "DocConcentration" -> "1.02, 1.5, 12.4",
      "FeaturesCol" -> "FeaturesCol",
      "K" -> "6",
      "MaxIter" -> "15",
      "Optimizer" -> "online",
      "Seed" -> "12454535",
      "SubsamplingRate" -> "0.066",
      "TopicConcentration" -> "0.23",
      "TopicDistributionCol" -> "gamma"
    )

    val LDAModel =  LDA(params)
    val LDAWithTwoParams = new SparkML()
      .setSeed(6464845)
      .setTopicDistributionCol("gamma")

    val expectedResult = List(
      new SparkML()
        .setCheckpointInterval(3)
        .setDocConcentration(Array(1.02, 1.5, 12.4))
        .setFeaturesCol("FeaturesCol")
        .setK(6)
        .setMaxIter(15)
        .setOptimizer("online")
        .setSeed(12454535)
        .setSubsamplingRate(0.066)
        .setTopicConcentration(0.23)
        .setTopicDistributionCol("gamma")
    )
    LDAModel.build().isSuccess shouldBe true

    val res = List(LDAModel.build().get)
    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head

    //    Test default values of unset params
    LDAWithTwoParams.getMaxIter shouldBe 20
    LDAWithTwoParams.getK shouldBe 10
    LDAWithTwoParams.getSubsamplingRate shouldBe 0.05

  }
  "GetMethode" should "Return the appropriate methode by it's name" in {
    val params = Map(
      "CheckpointInterval" -> "3",
      "DocConcentration" -> "1.02, 1.5, 12.4",
      "FeaturesCol" -> "FeaturesCol",
      "K" -> "6",
      "MaxIter" -> "15",
      "Optimizer" -> "online",
      "Seed" -> "12454535",
      "SubsamplingRate" -> "0.066",
      "TopicConcentration" -> "0.23",
      "TopicDistributionCol" -> "gamma"
    )
    val caraLDA = LDA(params)
    val model =caraLDA.build().get.asInstanceOf[SparkML]

    caraLDA.getMethode(model,10,"MaxIter").getName shouldBe "setMaxIter"
    caraLDA.getMethode(model,2,"K").getName shouldBe "setK"
    caraLDA.getMethode(model, "gamma" ,"TopicDistributionCol").getName shouldBe "setTopicDistributionCol"

  }


}
