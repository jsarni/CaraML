package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{CountVectorizerModel => fromSparkML}

import scala.util.Try

case class CountVectorizerModel(Binary:Option[Boolean],
                                InputCol:Option[String],
                                MaxDF:Option[Double],
                                MinDF:Option[Double],
                                MinTF:Option[Double],
                                OutputCol: Option[String],
                                VocabSize: Option[Int],
                                Vocabulary: Option[Array[String]]
                               ) extends CaraDataset {
  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(params.get("Binary").map(_.toBoolean),
      params.get("InputCol").map(_.toString),
      params.get("MaxDF").map(_.toDouble),
      params.get("MinDF").map(_.toDouble),
      params.get("MinTF").map(_.toDouble),
      params.get("OutputCol").map(_.toString),
      params.get("VocabSize").map(_.toInt),
      params.get("Vocabulary").map(_.toString.split(','))
    )
  }

  @Override
  def build(): Try[PipelineStage] = Try{

    val Dataset_feature=new fromSparkML(this.Vocabulary.getOrElse(Array.empty[String]))
    val ParamsToNotSet=List("Vocabulary","MaxDF","MinDF","VocabSize")
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined).filter(f=> ! ParamsToNotSet.contains(f.getName) )
    val names = definedFields.map(f => f.getName)
    println(names)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  GetMethode(Dataset_feature,f._2 match {case Some(s) => s },f._1).invoke(Dataset_feature,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    println("Succesfull")
    Dataset_feature
  }
}

object CountVectorizerModel {
  def apply(params: Map[String,String]):CountVectorizerModel= new CountVectorizerModel(params)
}



