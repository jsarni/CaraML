package io.github.jsarni.caraml.carastage.modelstage

import org.apache.spark.ml.regression.{GBTRegressor => SparkML}

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
 * @param Seed
 * @param WeightCol
 * @param FeatureSubsetStrategy
 * @param SubsamplingRate
 * @param LossType
 * @param MaxIter
 * @param StepSize
 * @param ValidationIndicatorCol
 * @param ValidationTol
 * @param Impurity
 */
case class GBTRegressor(CheckpointInterval: Option[Int],
                        FeaturesCol: Option[String],
                        LabelCol: Option[String],
                        LeafCol: Option[String],
                        MaxBins: Option[Int],
                        MaxDepth: Option[Int],
                        MinInfoGain: Option[Double],
                        MinInstancesPerNode: Option[Int],
                        MinWeightFractionPerNode: Option[Double],
                        PredictionCol: Option[String], Seed: Option[Long],
                        WeightCol: Option[String],
                        FeatureSubsetStrategy: Option[String],
                        SubsamplingRate: Option[Double],
                        LossType: Option[String],
                        MaxIter: Option[Int],
                        StepSize: Option[Double],
                        ValidationIndicatorCol: Option[String],
                        ValidationTol: Option[Double],
                        Impurity: Option[String])
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
      params.get("Seed").map(_.toLong),
      params.get("WeightCol"),
      params.get("FeatureSubsetStrategy"),
      params.get("SubsamplingRate").map(_.toDouble),
      params.get("LossType"),
      params.get("MaxIter").map(_.toInt),
      params.get("StepSize").map(_.toDouble),
      params.get("ValidationIndicatorCol"),
      params.get("ValidationTol").map(_.toDouble),
      params.get("Impurity")
    )
  }

}

object GBTRegressor {
  def apply(params: Map[String, String]): GBTRegressor = new GBTRegressor(params)
}

