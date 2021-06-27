package io.github.jsarni.CaraStage.ModelStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.classification.{NaiveBayes => SparkML}
import scala.util.Try

case class NaiveBayes(FeaturesCol : Option[String], LabelCol : Option[String], ModelType : Option[String], PredictionCol : Option[String], ProbabilityCol : Option[String],
                      RawPredictionCol : Option[String], Smoothing : Option[Double], Thresholds : Option[Array[Double]], WeightCol : Option[String])

  extends CaraModel {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("FeaturesCol"),
      params.get("LabelCol"),
      params.get("ModelType"),
      params.get("PredictionCol"),
      params.get("ProbabilityCol"),
      params.get("RawPredictionCol"),
      params.get("Smoothing").map(_.toDouble),
      params.get("Thresholds").map(_.split(",").map(_.toDouble)),
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
object NaiveBayes {
  def apply(params: Map[String, String]): NaiveBayes = new NaiveBayes(params)
}
