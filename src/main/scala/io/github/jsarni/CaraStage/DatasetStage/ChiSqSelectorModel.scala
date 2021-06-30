package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{ChiSqSelector => fromSparkML}

import scala.util.Try

case class ChiSqSelectorModel(Fdr: Option[Double],
                              FeaturesCol:Option[String],
                              Fpr: Option[Double],
                              Fwe: Option[Double],
                              LabelCol:Option[String],
                              NumTopFeatures: Option[Int],
                              OutputCol: Option[String],
                              Percentile:Option[Double],
                              SelectorType:Option[String],
                              SelectedFeatures: Option[Array[Int]]
) extends CaraDataset[fromSparkML] {
  @MapperConstructor
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

  @Override
  override def build(): Try[PipelineStage] = Try{

    val datasetFeature=new fromSparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values

    zipFields.map(f=> getMethode(datasetFeature, f._2 match {case Some(s) => s }, f._1)
                      .invoke(datasetFeature,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] }))
                 )
    datasetFeature
  }
}
object ChiSqSelectorModel {
  def apply(params : Map[String,String]): ChiSqSelectorModel = new ChiSqSelectorModel(params)
}



