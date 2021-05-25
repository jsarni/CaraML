package io.github.jsarni.CaraStage.DatasetStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.feature.{RegexTokenizer => fromSparkML}

import scala.util.Try

case class RegexTokenizer(Gaps:Option[Boolean],
                          InputCol:Option[String],
                          MinTokenLength: Option[Int],
                          OutputCol: Option[String],
                          Pattern: Option[String],
                          ToLowercase : Option[Boolean])
  extends CaraDataset {
  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("Gaps").map(_.toBoolean),
      params.get("InputCol"),
      params.get("MinTokenLength").map(_.toInt),
      params.get("OutputCol"),
      params.get("Pattern"),
      params.get("ToLowercase").map(_.toBoolean)
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
object RegexTokenizer {
  def apply(params: Map[String, String]): RegexTokenizer = new RegexTokenizer(params)

}
