package io.github.jsarni.CaraStage.DatasetStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{HashingTF => fromSparkML}

import scala.util.Try

case class HashingTF(Binary:Option[Boolean],
                     InputCol:Option[String],
                     NumFeatures:Option[Int],
                     OutputCol: Option[String]) extends CaraDataset {
  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("Binary").map(_.toBoolean),
      params.get("InputCol").map(_.toString),
      params.get("NumFeatures").map(_.toInt),
      params.get("OutputCol").map(_.toString)
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
object HashingTF {
  def apply(params: Map[String, String]): HashingTF = new HashingTF(params)

}
