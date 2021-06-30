package io.github.jsarni.CaraStage.ModelStage

import org.apache.spark.ml.classification.{NaiveBayes => SparkML}

case class NaiveBayes(FeaturesCol: Option[String],
                      LabelCol: Option[String],
                      ModelType: Option[String],
                      PredictionCol: Option[String],
                      ProbabilityCol: Option[String],
                      RawPredictionCol: Option[String],
                      Smoothing: Option[Double],
                      Thresholds: Option[Array[Double]],
                      WeightCol: Option[String])

  extends CaraModel[SparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("FeaturesCol"),
      params.get("LabelCol"),
      params.get("ModelType"),
      params.get("PredictionCol"),
      params.get("ProbabilityCol"),
      params.get("RawPredictionCol"),
      params.get("Smoothing").map(_.toDouble),
      params.get("Thresholds").map(_.split(",").map(_.toDouble)),
      params.get("WeightCol")

    )
  }

}

object NaiveBayes {
  def apply(params: Map[String, String]): NaiveBayes = new NaiveBayes(params)
}
