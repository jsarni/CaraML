package io.github.jsarni.CaraStage.DatasetStage

import org.apache.spark.ml.feature.{HashingTF => fromSparkML}

case class HashingTF(Binary: Option[Boolean],
                     InputCol: Option[String],
                     NumFeatures: Option[Int],
                     OutputCol: Option[String])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("Binary").map(_.toBoolean),
      params.get("InputCol"),
      params.get("NumFeatures").map(_.toInt),
      params.get("OutputCol")
    )
  }

}

object HashingTF {
  def apply(params: Map[String, String]): HashingTF = new HashingTF(params)
}

