package io.github.jsarni.CaraStage.DatasetStage

import org.apache.spark.ml.feature.{Bucketizer => fromSparkML}

case class Bucketizer(HandleInvalid: Option[String] = Option("error"),
                      InputCol: Option[String],
                      InputCols: Option[Array[String]],
                      OutputCol: Option[String],
                      OutputCols: Option[Array[String]],
                      Splits: Option[Array[Double]],
                      SplitsArray: Option[Array[Array[Double]]])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("HandleInvalid"),
      params.get("InputCol"),
      params.get("InputCols").map(_.split(',').map(_.trim)),
      params.get("OutputCol"),
      params.get("OutputCols").map(_.split(',').map(_.trim)),
      params.get("Splits").map(_.split(",").map(_.toDouble)),
      params.get("SplitsArray").map(_.split(';').map(_.split(',').map(_.toDouble)))
    )
  }

}

object Bucketizer{
  def apply(params: Map[String,String]): Bucketizer = new Bucketizer(params)
}


