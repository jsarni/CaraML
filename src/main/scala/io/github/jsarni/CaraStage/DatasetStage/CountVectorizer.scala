package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{CountVectorizer => fromSparkML}

import scala.util.Try

case class CountVectorizer(Binary:Option[Boolean],
                           InputCol:Option[String],
                           MaxDF:Option[Double],
                           MinDF:Option[Double],
                           MinTF:Option[Double],
                           OutputCol: Option[String],
                           VocabSize: Option[Int]) extends CaraDataset {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("Binary").map(_.toBoolean),
      params.get("InputCol"),
      params.get("MaxDF").map(_.toDouble),
      params.get("MinDF").map(_.toDouble),
      params.get("MinTF").map(_.toDouble),
      params.get("OutputCol"),
      params.get("VocabSize").map(_.toInt)
    )
  }

  @Override
  def build(): Try[PipelineStage] = Try{

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

object CountVectorizer {
  def apply(params: Map[String,String]):CountVectorizer= new CountVectorizer(params)
}

