package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.feature.{Word2Vec => fromSparkML}
import org.apache.spark.ml.PipelineStage

import scala.util.Try

case class Word2Vec (InputCol: Option[String],
                    MaxIter: Option[Int],
                    MaxSentenceLength:Option[Int],
                    NumPartitions: Option[Int],
                    OutputCol: Option[String],
                    Seed: Option[Long],
                    StepSize: Option[Double],
                    VectorSize: Option[Int],
                    MinCount: Option[Int])
  extends CaraDataset[fromSparkML] {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol"),
      params.get("MaxIter").map(_.toInt),
      params.get("MaxSentenceLength").map(_.toInt),
      params.get("NumPartitions").map(_.toInt),
      params.get("OutputCol"),
      params.get("Seed").map(_.toLong),
      params.get("StepSize").map(_.toDouble),
      params.get("VectorSize").map(_.toInt),
      params.get("MinCount").map(_.toInt)
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

object Word2Vec {
  def apply(params: Map[String, String]): Word2Vec = new Word2Vec(params)

}

