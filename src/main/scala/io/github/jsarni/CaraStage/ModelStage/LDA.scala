package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.clustering.{LDA => SparkML}
import scala.util.Try


case class LDA(CheckpointInterval : Option[Int], DocConcentration : Option[Array[Double]], FeaturesCol : Option[String], K : Option[Int], MaxIter : Option[Int],
               Optimizer : Option[String], Seed : Option[Long], SubsamplingRate : Option[Double], TopicConcentration : Option[Double], TopicDistributionCol : Option[String],
              )

  extends CaraModel[SparkML] {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("CheckpointInterval").map(_.toInt),
      params.get("DocConcentration").map(_.split(",").map(_.toDouble)),
      params.get("FeaturesCol"),
      params.get("K").map(_.toInt),
      params.get("MaxIter").map(_.toInt),
      params.get("Optimizer"),
      params.get("Seed").map(_.toLong),
      params.get("SubsamplingRate").map(_.toDouble),
      params.get("TopicConcentration").map(_.toDouble),
      params.get("TopicDistributionCol")
    )
  }

  override def build(): Try[PipelineStage] = Try {
    val model = new SparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map { f =>
      val fieldName = f._1
      val fieldValue = f._2 match {case Some(s) => s }
      getMethode(model,fieldValue,fieldName)
        .invoke(model,fieldValue.asInstanceOf[f._2.type])
    }
    model
  }
}
object LDA {
  def apply(params: Map[String, String]): LDA = new LDA(params)
}
