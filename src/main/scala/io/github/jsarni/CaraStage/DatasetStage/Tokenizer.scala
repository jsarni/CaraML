package io.github.jsarni.CaraStage.DatasetStage

import org.apache.spark.ml.feature.{Tokenizer => fromSparkML}

/**
 * @param InputCol
 * @param OutputCol
 */
case class Tokenizer(InputCol: Option[String],
                     OutputCol: Option[String])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol"),
      params.get("OutputCol")
    )
  }

}

object Tokenizer {
  def apply(params: Map[String, String]): Tokenizer = new Tokenizer(params)
}
