package io.github.jsarni.CaraStage.ModelStage

import org.apache.spark.ml.classification.{DecisionTreeClassifier => SparkML}

case class DecisionTreeClassifier(CheckpointInterval: Option[Int],
                                  FeaturesCol: Option[String],
                                  Impurity: Option[String],
                                  LabelCol: Option[String],
                                  LeafCol: Option[String],
                                  MaxBins: Option[Int],
                                  MaxDepth: Option[Int],
                                  MinInfoGain: Option[Double],
                                  MinInstancesPerNode: Option[Int],
                                  MinWeightFractionPerNode: Option[Double],
                                  PredictionCol: Option[String],
                                  ProbabilityCol: Option[String],
                                  RawPredictionCol: Option[String],
                                  Seed: Option[Long],
                                  Thresholds: Option[Array[Double]],
                                  WeightCol: Option[String])
  extends CaraModel[SparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("CheckpointInterval").map(_.toInt),
      params.get("FeaturesCol"),
      params.get("Impurity"),
      params.get("LabelCol"),
      params.get("LeafCol"),
      params.get("MaxBins").map(_.toInt),
      params.get("MaxDepth").map(_.toInt),
      params.get("MinInfoGain").map(_.toDouble),
      params.get("MinInstancesPerNode").map(_.toInt),
      params.get("MinWeightFractionPerNode").map(_.toDouble),
      params.get("PredictionCol"),
      params.get("ProbabilityCol"),
      params.get("RawPredictionCol"),
      params.get("Seed").map(_.toLong),
      params.get("Thresholds").map(_.split(",").map(_.toDouble)),
      params.get("WeightCol")
    )
  }

}

object DecisionTreeClassifier {
  def apply(params: Map[String, String]): DecisionTreeClassifier = new DecisionTreeClassifier(params)
}

