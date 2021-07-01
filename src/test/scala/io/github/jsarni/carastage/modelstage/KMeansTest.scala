package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.clustering.{KMeans => SparkML}
import io.github.jsarni.TestBase

class KMeansTest extends TestBase {
  "build" should "Create an lr model and set all parameters with there args values or set default ones" in {
    val params = Map(
      "DistanceMeasure" -> "euclidean",
      "FeaturesCol" -> "FeaturesCol",
      "K" -> "5",
      "MaxIter" -> "12",
      "PredictionCol" -> "PredictionCol",
      "Seed" -> "1214151",
      "Tol" -> "0.2",
      "WeightCol" -> "WeightColname"
    )

    val Kmeans = KMeans(params)
    val KmeansWithTwoParams = new SparkML()
      .setTol(0.3)
      .setDistanceMeasure("euclidean")

    val expectedResult = List(
      new SparkML()
        .setDistanceMeasure("euclidean")
        .setFeaturesCol("FeaturesCol")
        .setK(5)
        .setMaxIter(12)
        .setPredictionCol("PredictionCol")
        .setSeed(1214151)
        .setTol(0.2)
        .setWeightCol("WeightColname")
    )
    Kmeans.build().isSuccess shouldBe true

    val res = List(Kmeans.build().get)
    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head

    KmeansWithTwoParams.getMaxIter shouldBe 20
    KmeansWithTwoParams.getK shouldBe 2
  }

  "GetMethode" should "Return the appropriate methode by it's name" in {
    val params = Map(
      "DistanceMeasure" -> "euclidean",
      "FeaturesCol" -> "FeaturesCol",
      "K" -> "5",
      "MaxIter" -> "12",
      "PredictionCol" -> "PredictionCol",
      "Seed" -> "1214151",
      "Tol" -> "0.2",
      "WeightCol" -> "WeightColname"
    )
    val caraKmeans = KMeans(params)
    val model =caraKmeans.build().get.asInstanceOf[SparkML]

    caraKmeans.getMethode(model,10,"MaxIter").getName shouldBe "setMaxIter"
    caraKmeans.getMethode(model,2,"K").getName shouldBe "setK"
    caraKmeans.getMethode(model, "euclidean" ,"DistanceMeasure").getName shouldBe "setDistanceMeasure"
  }

}
