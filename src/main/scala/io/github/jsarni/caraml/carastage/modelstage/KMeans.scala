package io.github.jsarni.caraml.carastage.modelstage

import org.apache.spark.ml.clustering.{KMeans => SparkML}

/**
 * @param DistanceMeasure
 * @param FeaturesCol
 * @param K
 * @param MaxIter
 * @param PredictionCol
 * @param Seed
 * @param Tol
 * @param WeightCol
 */
case class KMeans(DistanceMeasure: Option[String],
                  FeaturesCol: Option[String],
                  K: Option[Int],
                  MaxIter: Option[Int],
                  PredictionCol: Option[String],
                  Seed: Option[Long],
                  Tol: Option[Double],
                  WeightCol: Option[String])
  extends CaraModel[SparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("DistanceMeasure"),
      params.get("FeaturesCol"),
      params.get("K").map(_.toInt),
      params.get("MaxIter").map(_.toInt),
      params.get("PredictionCol"),
      params.get("Seed").map(_.toLong),
      params.get("Tol").map(_.toDouble),
      params.get("WeightCol")

    )
  }

}

object KMeans {
  def apply(params: Map[String, String]): KMeans = new KMeans(params)
}
