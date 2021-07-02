package io.github.jsarni.caraml.carastage.modelstage

import io.github.jsarni.caraml.carastage.CaraStage
import org.apache.spark.ml.PipelineStage
import scala.reflect.ClassTag

abstract class CaraModel[T<: PipelineStage](implicit classTag: ClassTag[T]) extends CaraStage[T]{}
