package io.github.jsarni.CaraStage.DatasetStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{Tokenizer => fromSparkML}

import scala.util.Try
case class Tokenizer(InputCol:Option[String],
                     OutputCol: Option[String])
  extends CaraDataset {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol").map(_.toString),
      params.get("OutputCol").map(_.toString)
    )
  }
  @Override
  def build(): Try[PipelineStage] = Try{
    val Tokenizer=new fromSparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  GetMethode(Tokenizer,f._2 match {case Some(s) => s },f._1).invoke(Tokenizer,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    println("Succesfull")
    Tokenizer
  }
}
object Tokenizer {
  def apply(params: Map[String, String]): Tokenizer = new Tokenizer(params)

}