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
 * @param SelectedFeatures
 */
case class ChiSqSelectorModel(Fdr: Option[Double],
                              FeaturesCol: Option[String],
                              Fpr: Option[Double],
                              Fwe: Option[Double],
                              LabelCol: Option[String],
                              NumTopFeatures: Option[Int],
                              OutputCol: Option[String],
                              Percentile: Option[Double],
                              SelectorType: Option[String],
                              SelectedFeatures: Option[Array[Int]])
  extends CaraDataset[fromSparkML] {

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
      params.get("SelectorType"),
      params.get("SelectedFeatures").map(_.split(",").map(_.toInt))
    )
  }

}

object ChiSqSelectorModel {
  def apply(params : Map[String,String]): ChiSqSelectorModel = new ChiSqSelectorModel(params)
}



