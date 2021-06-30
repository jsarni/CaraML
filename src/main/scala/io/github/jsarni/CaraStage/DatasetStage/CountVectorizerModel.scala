package io.github.jsarni.CaraStage.DatasetStage

import org.apache.spark.ml.feature.{CountVectorizerModel => fromSparkML}

/**
 * @param Binary
 * @param InputCol
 * @param MaxDF
 * @param MinDF
 * @param MinTF
 * @param OutputCol
 * @param VocabSize
 * @param Vocabulary
 */
case class CountVectorizerModel(Binary: Option[Boolean],
                                InputCol: Option[String],
                                MaxDF: Option[Double],
                                MinDF: Option[Double],
                                MinTF: Option[Double],
                                OutputCol: Option[String],
                                VocabSize: Option[Int],
                                Vocabulary: Option[Array[String]])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("Binary").map(_.toBoolean),
      params.get("InputCol"),
      params.get("MaxDF").map(_.toDouble),
      params.get("MinDF").map(_.toDouble),
      params.get("MinTF").map(_.toDouble),
      params.get("OutputCol"),
      params.get("VocabSize").map(_.toInt),
      params.get("Vocabulary").map(_.split(','))
    )
  }

}

object CountVectorizerModel {
  def apply(params: Map[String,String]): CountVectorizerModel = new CountVectorizerModel(params)
}



