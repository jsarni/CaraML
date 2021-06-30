package io.github.jsarni.CaraStage.ModelStage
import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.regression.{LinearRegression => SparkLR}
import scala.util.Try



case class LinearRegression(MaxIter: Option[Int], RegParam: Option[Double], ElasticNetParam: Option[Double], LabelCol:Option[String], Loss: Option[String],
                            FitIntercept: Option[Boolean], PredictionCol: Option[String], FeaturesCol: Option[String], Solver: Option[String],
                            Standardization: Option[Boolean], Tol : Option[Double], WeightCol: Option[String])

  extends CaraModel[SparkLR] {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("MaxIter").map(_.toInt),
      params.get("RegParam").map(_.toDouble),
      params.get("ElasticNetParam").map(_.toDouble),
      params.get("LabelCol"),
      params.get("Loss"),
      params.get("FitIntercept").map(_.toBoolean),
      params.get("PredictionCol"),
      params.get("FeaturesCol"),
      params.get("Solver"),
      params.get("Standardization").map(_.toBoolean),
      params.get("Tol").map(_.toDouble),
      params.get("WeightCol")

    )
  }

  override def build(): Try[PipelineStage] = Try {
    val lr = new SparkLR()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  getMethode(lr,f._2 match {case Some(s) => s },f._1).invoke(lr,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    lr

  }
}
object LinearRegression
{
  def apply(params: Map[String, String]): LinearRegression = new LinearRegression(params)
}





