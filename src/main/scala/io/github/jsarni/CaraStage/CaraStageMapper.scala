package io.github.jsarni.CaraStage

import io.github.jsarni.CaraStage.DatasetStage.CaraDataset
import io.github.jsarni.CaraStage.ModelStage._

import scala.util.Try

trait CaraStageMapper {

  def mapStage(stageDescription: CaraStageDescription): CaraStage = {
    Try(mapModelStage(stageDescription)).getOrElse(mapDatasetStage(stageDescription))
  }

  def mapModelStage(stageDescription: CaraStageDescription): CaraModel = {
    stageDescription.stageName match {
      case "TestStage" => TestStage(stageDescription.params)
      case _ => throw
        new Exception(s"${stageDescription.stageName} is not a valid Cara Stage name. Please verify your Yaml File")
    }
  }

  def mapDatasetStage(stageDescription: CaraStageDescription): CaraDataset = {
    stageDescription.stageName match {
      case _ =>
        throw
          new Exception(s"${stageDescription.stageName} is not a valid Cara Stage name. Please verify your Yaml File")
    }
  }

}
