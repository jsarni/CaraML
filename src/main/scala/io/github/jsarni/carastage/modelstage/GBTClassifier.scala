package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.classification.{GBTClassifier => SparkML}

/**
 * @param CheckpointInterval
 * @param FeaturesCol
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
 * @param LossType
 * @param MaxIter
 * @param StepSize
 * @param ValidationIndicatorCol
 */
case class GBTClassifier(CheckpointInterval: Option[Int],
                         FeaturesCol: Option[String],
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
                         LossType: Option[String],
                         MaxIter: Option[Int],
                          StepSize: Option[Double],
                         ValidationIndicatorCol: Option[String])
  extends CaraModel[SparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("CheckpointInterval").map(_.toInt),
      params.get("FeaturesCol"),
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
      params.get("LossType"),
      params.get("MaxIter").map(_.toInt),
      params.get("StepSize").map(_.toDouble),
      params.get("ValidationIndicatorCol")
    )
  }

}

object GBTClassifier {
  def apply(params: Map[String, String]): GBTClassifier = new GBTClassifier(params)
}
