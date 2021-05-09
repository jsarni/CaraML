package io.github.jsarni.CaraStage

import io.github.jsarni.CaraStage.DatasetStage.CaraDataset
import io.github.jsarni.CaraStage.ModelStage.{CaraModel, LogisticRegression}

trait CaraStageMapper {

  def mapModelStage(stageDescription: CaraStageDescription): CaraModel = {
    stageDescription.stageName match {
      case "LogisticRegression" => LogisticRegression(stageDescription.params)
      case _ => throw
        new Exception(s"${stageDescription.stageName} is not a valid Model stage name. Please verify the corresponding Yaml File")

    }
  }

  def mapDatasetStage(stageDescription: CaraStageDescription): CaraDataset = {
    stageDescription.stageName match {
      case _ =>
        throw
          new Exception(s"${stageDescription.stageName} is not a valid Dataset stage name. Please verify the corresponding Yaml File")
    }
  }

}
