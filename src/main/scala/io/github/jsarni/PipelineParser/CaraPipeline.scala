package io.github.jsarni.PipelineParser

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.Evaluator

case class CaraPipeline(pipeline: Pipeline, evaluator: Evaluator)
