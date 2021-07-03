package io.github.jsarni.caraml.carastage.modelstage

import org.apache.spark.ml.clustering.{LDA => SparkML}

/**
 * @param CheckpointInterval
 * @param DocConcentration
 * @param FeaturesCol
 * @param K
 * @param MaxIter
 * @param Optimizer
 * @param Seed
 * @param SubsamplingRate
 * @param TopicConcentration
 * @param TopicDistributionCol
 */
case class LDA(CheckpointInterval: Option[Int],
               DocConcentration: Option[Array[Double]],
               FeaturesCol: Option[String],
               K: Option[Int],
               MaxIter: Option[Int],
               Optimizer: Option[String],
               Seed: Option[Long],
               SubsamplingRate: Option[Double],
               TopicConcentration: Option[Double],
               TopicDistributionCol: Option[String])
  extends CaraModel[SparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("CheckpointInterval").map(_.toInt),
      params.get("DocConcentration").map(_.split(",").map(_.toDouble)),
      params.get("FeaturesCol"),
      params.get("K").map(_.toInt),
      params.get("MaxIter").map(_.toInt),
      params.get("Optimizer"),
      params.get("Seed").map(_.toLong),
      params.get("SubsamplingRate").map(_.toDouble),
      params.get("TopicConcentration").map(_.toDouble),
      params.get("TopicDistributionCol")
    )
  }

}

object LDA {
  def apply(params: Map[String, String]): LDA = new LDA(params)
}
