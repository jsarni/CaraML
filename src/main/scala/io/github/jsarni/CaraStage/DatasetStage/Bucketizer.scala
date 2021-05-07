package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{Bucketizer => fromSparkML}

case class Bucketizer( HandleInvalid: Option[String],
                       InputCol:Option[String],
                       InputCols:Option[Array[String]],
                       OutputCol: Option[String],
                       OutputCols: Option[Array[String]],
                       Splits: Option[Array[Double]],
                       SplitsArray: Option[Array[Double]]) extends CaraDataset {
  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("HandleInvalid").map(_.toString),
      params.get("InputCol").map(_.toString()),
      params.get("InputCols").map(_.split(',').map(_.trim)),
      params.get("OutputCol").map(_.toString),
      params.get("OutputCols").map(_.toString.split(',').map(_.trim)),
      params.get("Splits").map(_.split(",").map(_.toDouble)),
      params.get("SplitsArray").map(_.split(",").map(_.toDouble))
    )
  }

  @Override
  def build(): PipelineStage = {
    val Dataset_feature=new fromSparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  GetMethode(Dataset_feature,f._2 match {case Some(s) => s },f._1).invoke(Dataset_feature,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    println("Succesfull")
    Dataset_feature
  }
}
object Bucketizer{
  def apply(params: Map[String,String]): Bucketizer = new Bucketizer(params)
}


