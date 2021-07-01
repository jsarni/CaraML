package io.github.jsarni.pipelineparser

import io.github.jsarni.carastage.tuningstage.TuningStageDescription
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.Evaluator

case class CaraPipeline(pipeline: Pipeline, evaluator: Evaluator, tuner: Option[TuningStageDescription])
