package io.github.jsarni.caraml.carastage.datasetstage

import org.apache.spark.ml.feature.{ChiSqSelector => fromSparkML}

/**
 * @param Fdr
 * @param FeaturesCol
 * @param Fpr
 * @param Fwe
 * @param LabelCol
 * @param NumTopFeatures
 * @param OutputCol
 * @param Percentile
 * @param SelectorType
 */
case class ChiSqSelector(Fdr: Option[Double],
                         FeaturesCol:Option[String],
                         Fpr: Option[Double],
                         Fwe: Option[Double],
                         LabelCol:Option[String],
                         NumTopFeatures: Option[Int],
                         OutputCol: Option[String],
                         Percentile:Option[Double],
                         SelectorType:Option[String]
) extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("Fdr").map(_.toDouble),
      params.get("FeaturesCol"),
      params.get("Fpr").map(_.toDouble),
      params.get("Fwe").map(_.toDouble),
      params.get("LabelCol"),
      params.get("NumTopFeatures").map(_.toInt),
      params.get("OutputCol"),
      params.get("Percentile").map(_.toDouble),
      params.get("SelectorType")
    )
  }

}

object ChiSqSelector{
  def apply(params: Map[String,String]): ChiSqSelector = new ChiSqSelector(params)
}



