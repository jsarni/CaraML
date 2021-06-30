package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.CaraStage.CaraStage
import org.apache.spark.ml.PipelineStage

import scala.reflect.ClassTag

abstract class CaraDataset[T <: PipelineStage](implicit classTag: ClassTag[T]) extends CaraStage[T]{

}
