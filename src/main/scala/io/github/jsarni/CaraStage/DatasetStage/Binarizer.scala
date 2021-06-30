package io.github.jsarni.CaraStage.DatasetStage

import org.apache.spark.ml.feature.{Binarizer => fromSparkML}

case class Binarizer(InputCol: Option[String],
                     InputCols: Option[Array[String]],
                     OutputCol: Option[String],
                     OutputCols: Option[Array[String]],
                     Threshold: Option[Double],
                     Thresholds: Option[Array[Double]])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol"),
      params.get("InputCols").map(_.split(',').map(_.trim)),
      params.get("OutputCol"),
      params.get("OutputCols").map(_.split(',').map(_.trim)),
      params.get("Threshold").map(_.toDouble),
      params.get("Thresholds").map(_.split(",").map(_.toDouble))
    )
  }
}

object Binarizer{
  def apply(params: Map[String,String]): Binarizer = new Binarizer(params)
}