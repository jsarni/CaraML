package io.github.jsarni.caraml.carastage.datasetstage

import org.apache.spark.ml.feature.{CountVectorizer => fromSparkML}

/**
 * @param Binary
 * @param InputCol
 * @param MaxDF
 * @param MinDF
 * @param MinTF
 * @param OutputCol
 * @param VocabSize
 */
case class CountVectorizer(Binary: Option[Boolean],
                           InputCol: Option[String],
                           MaxDF: Option[Double],
                           MinDF: Option[Double],
                           MinTF: Option[Double],
                           OutputCol: Option[String],
                           VocabSize: Option[Int])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("Binary").map(_.toBoolean),
      params.get("InputCol"),
      params.get("MaxDF").map(_.toDouble),
      params.get("MinDF").map(_.toDouble),
      params.get("MinTF").map(_.toDouble),
      params.get("OutputCol"),
      params.get("VocabSize").map(_.toInt)
    )
  }

}

object CountVectorizer {
  def apply(params: Map[String,String]): CountVectorizer = new CountVectorizer(params)
}

