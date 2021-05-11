package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{Binarizer => fromSparkML}

import scala.util.Try

case class Binarizer(InputCol:Option[String],
                     InputCols:Option[Array[String]],
                     OutputCol: Option[String],
                     OutputCols: Option[Array[String]],
                     Threshold: Option[Double],
                     Thresholds: Option[Array[Double]]) extends CaraDataset {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol").map(_.toString()),
      params.get("InputCols").map(_.split(',').map(_.trim)),
      params.get("OutputCol").map(_.toString),
      params.get("OutputCols").map(_.toString.split(',').map(_.trim)),
      params.get("Threshold").map(_.toDouble),
      params.get("Thresholds").map(_.split(",").map(_.toDouble))
    )
  }

  @Override
  def build(): Try[PipelineStage]= Try{
    val Binarize=new fromSparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  GetMethode(Binarize,f._2 match {case Some(s) => s },f._1).invoke(Binarize,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    Binarize
  }
}
object Binarizer{
  def apply(params: Map[String,String]): Binarizer = new Binarizer(params)
}


