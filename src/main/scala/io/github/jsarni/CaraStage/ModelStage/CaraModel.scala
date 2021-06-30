package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.CaraStage
import org.apache.spark.ml.PipelineStage

import scala.reflect.ClassTag

abstract class CaraModel[T<: PipelineStage](implicit classTag: ClassTag[T]) extends CaraStage[T]{

}
