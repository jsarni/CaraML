package io.github.jsarni.CaraStage

import io.github.jsarni.CaraStage.DatasetStage.CaraDataset
import io.github.jsarni.CaraStage.ModelStage._
import io.github.jsarni.CaraStage.TuningStage.TuningStageDescription
import org.apache.spark.ml.evaluation._

import scala.util.{Try, Success, Failure}

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

  def mapTuner(tuningStageDesc: TuningStageDescription): TuningStageDescription = {
    tuningStageDesc.tuningStage match {
      case "CrossValidator" =>
        if (!tuningStageDesc.paramName.equals("NumFolds"))
          throw new IllegalArgumentException("The only parameter available for CrossValidator is NumFolds")
        Try(tuningStageDesc.paramValue.toInt) match {
          case Success(_) =>
            tuningStageDesc
          case Failure(_) =>
            throw new IllegalArgumentException("The NumFolds parameter value must be an Integer")
        }
      case "TrainValidationSplit" =>
        if (!tuningStageDesc.paramName.equals("TrainRatio"))
          throw new IllegalArgumentException("The only parameter available for TrainValidationSplit is TrainRatio")
        Try(tuningStageDesc.paramValue.toDouble) match {
          case Success(value) =>
            if (value < 1 || value > 0)
              tuningStageDesc
            else
              throw new IllegalArgumentException("The TrainRation parameter value must be a Double between 0 and 1")
          case Failure(_) =>
            throw new IllegalArgumentException("The TrainRation parameter value must be a Double between 0 and 1")
        }
    }
  }

}
