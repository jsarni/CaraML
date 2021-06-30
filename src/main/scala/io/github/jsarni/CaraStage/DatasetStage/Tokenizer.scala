package io.github.jsarni.CaraStage.DatasetStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{Tokenizer => fromSparkML}

import scala.util.Try
case class Tokenizer(InputCol:Option[String],
                     OutputCol: Option[String])
  extends CaraDataset[fromSparkML] {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("InputCol"),
      params.get("OutputCol")
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
object Tokenizer {
  def apply(params: Map[String, String]): Tokenizer = new Tokenizer(params)

}
