package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor


class TestStage(MaxIter: Option[Int], RegParam: Option[Float], ElasticNetParam: Option[Float])
  extends CaraModel {

  @MapperConstructor
  def this(params: Map[String, String]) =
    this(
      params.get("MaxIter").map(_.toInt),
      params.get("RegParam").map(_.toFloat),
      params.get("ElasticNetParam").map(_.toFloat)
    )

  def print() = {
    println(MaxIter)
    println(RegParam)
    println(ElasticNetParam)
  }
}

object TestStage {
  def apply(params: Map[String, String]): TestStage = new TestStage(params)
}

