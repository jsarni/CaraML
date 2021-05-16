package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{ChiSqSelector => fromSparkML}

import scala.util.Try

case class ChiSqSelector(Fdr: Option[Double],
                         FeaturesCol:Option[String],
                         Fpr: Option[Double],
                         Fwe: Option[Double],
                         LabelCol:Option[String],
                         NumTopFeatures: Option[Int],
                         OutputCol: Option[String],
                         Percentile:Option[Double],
                         SelectorType:Option[String]
) extends CaraDataset {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("Fdr").map(_.toDouble),
      params.get("FeaturesCol").map(_.toString),
      params.get("Fpr").map(_.toDouble),
      params.get("Fwe").map(_.toDouble),
      params.get("LabelCol").map(_.toString),
      params.get("NumTopFeatures").map(_.toInt),
      params.get("OutputCol").map(_.toString),
      params.get("Percentile").map(_.toDouble),
      params.get("SelectorType").map(_.toString)
    )
  }

  @Override
  def build(): Try[PipelineStage] = Try{
    val datasetFeature=new fromSparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  getMethode(datasetFeature,f._2 match {case Some(s) => s },f._1)
             .invoke(datasetFeature,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    datasetFeature
  }
}
object ChiSqSelector{
  def apply(params: Map[String,String]): ChiSqSelector = new ChiSqSelector(params)
}



