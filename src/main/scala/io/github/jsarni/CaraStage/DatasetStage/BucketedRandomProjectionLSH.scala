package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{BucketedRandomProjectionLSH => fromSparkML}

case class BucketedRandomProjectionLSH(BucketLength: Option[Double],
                                        InputCol: Option[String],
                                       NumHashTables: Option[Int],
                                       OutputCol: Option[String],
                                       Seed: Option[Long])
  extends CaraDataset {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("BucketLength").map(_.toDouble),
      params.get("InputCol").map(_.toString),
      params.get("NumHashTables").map(_.toInt),
      params.get("OutputCol").map(_.toString),
      params.get("Seed").map(_.toLong)
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
object BucketedRandomProjectionLSH {
  def apply(params : Map[String,String]): BucketedRandomProjectionLSH = new BucketedRandomProjectionLSH(params)
}


