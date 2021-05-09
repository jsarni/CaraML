package io.github.jsarni.CaraStage

import org.apache.spark.ml.PipelineStage

import scala.util.Try

trait CaraStage {

  //TODO: Add builder function
  def build(): Try[PipelineStage]
}
