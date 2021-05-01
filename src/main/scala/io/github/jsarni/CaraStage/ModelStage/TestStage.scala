package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.types.StructType
import org.apache.spark.ml.classification.{LogisticRegression => logi}

case class TestStage(MaxIter: Option[Int], RegParam: Option[Double], ElasticNetParam: Option[Double])
  extends CaraModel {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("MaxIter").map(_.toInt),
      params.get("RegParam").map(_.toDouble),
      params.get("ElasticNetParam").map(_.toDouble)
    )
  }

  override def build(): PipelineStage = {
    val logistic= new logi()
    logistic
}
}

object TestStage {
  def apply(params: Map[String, String]): TestStage = new TestStage(params)
}

