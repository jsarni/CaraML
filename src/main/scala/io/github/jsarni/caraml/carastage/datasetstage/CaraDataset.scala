package io.github.jsarni.caraml.carastage.datasetstage

import io.github.jsarni.caraml.carastage.CaraStage
import org.apache.spark.ml.PipelineStage

import scala.reflect.ClassTag

abstract class CaraDataset[T <: PipelineStage](implicit classTag: ClassTag[T]) extends CaraStage[T]{}
