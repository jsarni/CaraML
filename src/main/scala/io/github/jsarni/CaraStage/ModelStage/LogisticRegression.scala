package io.github.jsarni.CaraStage.ModelStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.classification.{LogisticRegression => log}
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
      params.get("Family").map(_.toString),
      params.get("FeaturesCol").map(_.toString),
      params.get("FitIntercept").map(_.toBoolean),
      params.get("PredictionCol").map(_.toString),
      params.get("ProbabilityCol").map(_.toString),
      params.get("RawPredictionCol").map(_.toString),
      params.get("Standardization").map(_.toBoolean),
      params.get("Thresholds").map(_.split(",").map(_.toDouble)),
      params.get("Tol").map(_.toDouble),
      params.get("WeightCol").map(_.toString)

    )
  }

  override def build(): Try[PipelineStage] = Try {
    val lr = new log()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  GetMethode(lr,f._2 match {case Some(s) => s },f._1).invoke(lr,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    lr

  }
}
object LogisticRegression {
  def apply(params: Map[String, String]): LogisticRegression = new LogisticRegression(params)
}





