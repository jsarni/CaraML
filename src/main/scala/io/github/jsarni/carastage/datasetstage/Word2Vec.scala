package io.github.jsarni.carastage.datasetstage

import org.apache.spark.ml.feature.{Word2Vec => fromSparkML}

/**
 * @param InputCol
 * @param MaxIter
 * @param MaxSentenceLength
 * @param NumPartitions
 * @param OutputCol
 * @param Seed
 * @param StepSize
 * @param VectorSize
 * @param MinCount
 */
case class Word2Vec (InputCol: Option[String],
                    MaxIter: Option[Int],
                    MaxSentenceLength: Option[Int],
                    NumPartitions: Option[Int],
                    OutputCol: Option[String],
                    Seed: Option[Long],
                    StepSize: Option[Double],
                    VectorSize: Option[Int],
                    MinCount: Option[Int])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol"),
      params.get("MaxIter").map(_.toInt),
      params.get("MaxSentenceLength").map(_.toInt),
      params.get("NumPartitions").map(_.toInt),
      params.get("OutputCol"),
      params.get("Seed").map(_.toLong),
      params.get("StepSize").map(_.toDouble),
      params.get("VectorSize").map(_.toInt),
      params.get("MinCount").map(_.toInt)
    )
  }

}

object Word2Vec {
  def apply(params: Map[String, String]): Word2Vec = new Word2Vec(params)
}

