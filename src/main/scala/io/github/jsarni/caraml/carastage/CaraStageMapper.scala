package io.github.jsarni.caraml.carastage

import io.github.jsarni.caraml.carastage.datasetstage.CaraDataset
import io.github.jsarni.caraml.carastage.modelstage._
import io.github.jsarni.caraml.carastage.tuningstage.TuningStageDescription
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.evaluation._

import scala.util.{Failure, Success, Try}

trait CaraStageMapper {

  def mapStage(stageDescription: CaraStageDescription): Try[CaraStage[_ <: PipelineStage]] = Try {
    Try(mapModelStage(stageDescription)).getOrElse(mapDatasetStage(stageDescription))
  }

  def mapModelStage(stageDescription: CaraStageDescription): CaraModel[_ <: PipelineStage] = {
    stageDescription.stageName match {
      case "LogisticRegression" =>
        LogisticRegression(stageDescription.params)
      case "RandomForestClassifier" =>
        RandomForestClassifier(stageDescription.params)
      case "LinearRegression" =>
        LinearRegression(stageDescription.params)
      case "GBTClassifier" =>
        GBTClassifier(stageDescription.params)
      case "DecisionTreeClassifier" =>
        DecisionTreeClassifier(stageDescription.params)
      case "KMeans" =>
        KMeans(stageDescription.params)
      case "LDA" =>
        LDA(stageDescription.params)
      case "NaiveBayes" =>
        NaiveBayes(stageDescription.params)
      case "DecisionTreeRegressor" =>
        DecisionTreeRegressor(stageDescription.params)
      case "RandomForestRegressor" =>
        RandomForestRegressor(stageDescription.params)
      case "GBTRegressor" =>
        GBTRegressor(stageDescription.params)
      case _ => throw
        new Exception(s"${stageDescription.stageName} is not a valid Cara Stage name. Please verify your Yaml File")
    }
  }

  def mapDatasetStage(stageDescription: CaraStageDescription): CaraDataset[_ <: PipelineStage] = {
    stageDescription.stageName match {
      case _ =>
        throw
          new Exception(s"${stageDescription.stageName} is not a valid Cara Stage name. Please verify your Yaml File")
    }
  }

  def mapEvaluator(evaluatorName: String): Evaluator = {
    evaluatorName match {
      case "RegressionEvaluator" => new RegressionEvaluator()
      case "MulticlassClassificationEvaluator" => new MulticlassClassificationEvaluator()
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
