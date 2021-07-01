package io.github.jsarni.carastage.modelstage

import org.apache.spark.ml.regression.{LinearRegression => SparkLR}

/**
 * @param MaxIter
 * @param RegParam
 * @param ElasticNetParam
 * @param LabelCol
 * @param Loss
 * @param FitIntercept
 * @param PredictionCol
 * @param FeaturesCol
 * @param Solver
 * @param Standardization
 * @param Tol
 * @param WeightCol
 */
case class LinearRegression(MaxIter: Option[Int],
                            RegParam: Option[Double],
                            ElasticNetParam: Option[Double],
                            LabelCol:Option[String],
                            Loss: Option[String],
                            FitIntercept: Option[Boolean],
                            PredictionCol: Option[String],
                            FeaturesCol: Option[String],
                            Solver: Option[String],
                            Standardization: Option[Boolean],
                            Tol: Option[Double],
                            WeightCol: Option[String])

  extends CaraModel[SparkLR] {

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

}

object LinearRegression {
  def apply(params: Map[String, String]): LinearRegression = new LinearRegression(params)
}





