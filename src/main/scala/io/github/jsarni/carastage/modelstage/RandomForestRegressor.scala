package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.regression.{RandomForestRegressor => SparkML}

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
 * @param Seed
 * @param WeightCol
 * @param FeatureSubsetStrategy
 * @param SubsamplingRate
 * @param NumTrees
 */
case class RandomForestRegressor(CheckpointInterval: Option[Int],
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
                                 Seed: Option[Long],
                                 WeightCol: Option[String],
                                 FeatureSubsetStrategy: Option[String],
                                 SubsamplingRate: Option[Double],
                                 NumTrees: Option[Int])
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
      params.get("Seed").map(_.toLong),
      params.get("WeightCol"),
      params.get("FeatureSubsetStrategy"),
      params.get("SubsamplingRate").map(_.toDouble),
      params.get("NumTrees").map(_.toInt)
    )
  }

}
object RandomForestRegressor {
  def apply(params: Map[String, String]): RandomForestRegressor = new RandomForestRegressor(params)
}

