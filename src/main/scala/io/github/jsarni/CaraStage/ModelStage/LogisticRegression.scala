package io.github.jsarni.CaraStage.ModelStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.classification.{LogisticRegression => SparkML}
import scala.util.Try



case class LogisticRegression(MaxIter: Option[Int], RegParam: Option[Double], ElasticNetParam: Option[Double], Family:Option[String],FeaturesCol: Option[String]
                              , FitIntercept: Option[Boolean], PredictionCol: Option[String], ProbabilityCol: Option[String], RawPredictionCol: Option[String]
                              , Standardization: Option[Boolean], Thresholds: Option[Array[Double]] , Tol : Option[Double], WeightCol: Option[String])

  extends CaraModel {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("MaxIter").map(_.toInt),
      params.get("RegParam").map(_.toDouble),
      params.get("ElasticNetParam").map(_.toDouble),
      params.get("Family"),
      params.get("FeaturesCol"),
      params.get("FitIntercept").map(_.toBoolean),
      params.get("PredictionCol"),
      params.get("ProbabilityCol"),
      params.get("RawPredictionCol"),
      params.get("Standardization").map(_.toBoolean),
      params.get("Thresholds").map(_.split(",").map(_.toDouble)),
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
object LogisticRegression {
  def apply(params: Map[String, String]): LogisticRegression = new LogisticRegression(params)
}





