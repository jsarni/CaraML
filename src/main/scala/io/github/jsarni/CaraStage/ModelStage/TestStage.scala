package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor


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
}

object TestStage {
  def apply(params: Map[String, String]): TestStage = new TestStage(params)
}

