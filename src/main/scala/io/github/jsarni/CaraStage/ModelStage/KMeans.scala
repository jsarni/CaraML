package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.clustering.{KMeans => SparkML}
import scala.util.Try


case class KMeans(DistanceMeasure : Option[String], FeaturesCol : Option[String], K : Option[Int], MaxIter : Option[Int],
                  PredictionCol : Option[String], Seed : Option[Long], Tol : Option[Double], WeightCol : Option[String] )

  extends CaraModel[SparkML] {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("DistanceMeasure"),
      params.get("FeaturesCol"),
      params.get("K").map(_.toInt),
      params.get("MaxIter").map(_.toInt),
      params.get("PredictionCol"),
      params.get("Seed").map(_.toLong),
      params.get("Tol").map(_.toDouble),
      params.get("WeightCol")

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
object KMeans {
  def apply(params: Map[String, String]): KMeans = new KMeans(params)
}
