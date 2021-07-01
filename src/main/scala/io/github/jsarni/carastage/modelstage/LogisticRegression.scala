package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.classification.{LogisticRegression => SparkML}

/**
 * @param MaxIter
 * @param RegParam
 * @param ElasticNetParam
 * @param Family
 * @param FeaturesCol
 * @param FitIntercept
 * @param PredictionCol
 * @param ProbabilityCol
 * @param RawPredictionCol
 * @param Standardization
 * @param Thresholds
 * @param Tol
 * @param WeightCol
 */
case class LogisticRegression(MaxIter: Option[Int],
                              RegParam: Option[Double],
                              ElasticNetParam: Option[Double],
                              Family:Option[String],
                              FeaturesCol: Option[String],
                              FitIntercept: Option[Boolean],
                              PredictionCol: Option[String],
                              ProbabilityCol: Option[String],
                              RawPredictionCol: Option[String],
                              Standardization: Option[Boolean],
                              Thresholds: Option[Array[Double]],
                              Tol: Option[Double],
                              WeightCol: Option[String])
  extends CaraModel[SparkML] {

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

}

object LogisticRegression {
  def apply(params: Map[String, String]): LogisticRegression = new LogisticRegression(params)
}





