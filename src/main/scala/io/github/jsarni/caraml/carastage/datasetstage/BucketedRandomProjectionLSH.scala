package io.github.jsarni.caraml.carastage.datasetstage

import org.apache.spark.ml.feature.{BucketedRandomProjectionLSH => fromSparkML}

/**
 * @param BucketLength
 * @param InputCol
 * @param NumHashTables
 * @param OutputCol
 * @param Seed
 */
case class BucketedRandomProjectionLSH(BucketLength: Option[Double],
                                       InputCol: Option[String],
                                       NumHashTables: Option[Int],
                                       OutputCol: Option[String],
                                       Seed: Option[Long])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("BucketLength").map(_.toDouble),
      params.get("InputCol"),
      params.get("NumHashTables").map(_.toInt),
      params.get("OutputCol"),
      params.get("Seed").map(_.toLong)
    )
  }

}

object BucketedRandomProjectionLSH {
  def apply(params: Map[String,String]): BucketedRandomProjectionLSH = new BucketedRandomProjectionLSH(params)
}


