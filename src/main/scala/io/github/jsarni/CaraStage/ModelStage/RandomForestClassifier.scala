package io.github.jsarni.CaraStage.ModelStage

import org.apache.spark.ml.classification.{RandomForestClassifier => SparkML}

/**
 * @param CheckpointInterval
 * @param FeaturesCol
 * @param Impurity
 * @param LabelCol
 * @param LeafCol
 * @param MaxBins
 * @param MaxDepth
 * @param MinInfoGain
 * @param MinInstancesPerNode
 * @param MinWeightFractionPerNode
 * @param PredictionCol
 * @param ProbabilityCol
 * @param RawPredictionCol
 * @param Seed
 * @param Thresholds
 * @param WeightCol
 * @param FeatureSubsetStrategy
 * @param SubsamplingRate
 * @param NumTrees
 */
case class RandomForestClassifier(CheckpointInterval: Option[Int],
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
                                   WeightCol: Option[String],
                                  FeatureSubsetStrategy: Option[String],
                                  SubsamplingRate: Option[Double],
                                  NumTrees: Option[Int]
                                 )
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
      params.get("WeightCol"),
      params.get("FeatureSubsetStrategy"),
      params.get("SubsamplingRate").map(_.toDouble),
      params.get("NumTrees").map(_.toInt)
    )
  }

}

object RandomForestClassifier {
  def apply(params: Map[String, String]): RandomForestClassifier = new RandomForestClassifier(params)
}


