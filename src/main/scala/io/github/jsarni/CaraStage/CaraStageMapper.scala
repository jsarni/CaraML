package io.github.jsarni.CaraStage

import io.github.jsarni.CaraStage.DatasetStage.CaraDataset
import io.github.jsarni.CaraStage.ModelStage._

import org.apache.spark.ml.evaluation._

import scala.util.Try

trait CaraStageMapper {

  def mapStage(stageDescription: CaraStageDescription): Try[CaraStage] = Try {
    Try(mapModelStage(stageDescription)).getOrElse(mapDatasetStage(stageDescription))
  }

  def mapModelStage(stageDescription: CaraStageDescription): CaraModel = {
    stageDescription.stageName match {
      case "LogisticRegression" =>
        LogisticRegression(stageDescription.params)
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

  def mapEvaluator(evaluatorName: String): Evaluator = {
    evaluatorName match {
      case "RegressionEvaluator" => new RegressionEvaluator()
      case _ =>
        throw
          new Exception(s"${evaluatorName} is not a valid SparkML Validator name. Please verify your Yaml File")
    }
  }

}
