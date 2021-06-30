package io.github.jsarni.CaraStage.DatasetStage

import org.apache.spark.ml.feature.{IDF => fromSparkML}

/**
 * @param InputCol
 * @param MinDocFreq
 * @param OutputCol
 */
case class IDF(InputCol: Option[String],
               MinDocFreq: Option[Int],
               OutputCol: Option[String])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol"),
      params.get("MinDocFreq").map(_.toInt),
      params.get("OutputCol")
    )
  }

}

object IDF {
  def apply(params: Map[String, String]): IDF = new IDF(params)
}