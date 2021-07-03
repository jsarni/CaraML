package io.github.jsarni.caraml.pipelineparser

import com.fasterxml.jackson.databind.JsonNode
import io.github.jsarni.caraml.carastage.tuningstage.TuningStageDescription
import io.github.jsarni.caraml.carastage.{CaraStage, CaraStageDescription, CaraStageMapper}
import io.github.jsarni.caraml.carayaml.CaraYamlReader
import org.apache.spark.ml.evaluation.Evaluator
import org.apache.spark.ml.{Pipeline, PipelineStage}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class CaraParser(caraYaml: CaraYamlReader) extends CaraStageMapper{

  val contentTry = caraYaml.loadFile()

  def build(): Try[CaraPipeline] = {
    for {
      pipeline <- parsePipeline()
      evaluator <- parseEvaluator()
      hasTuner <- hasTuner()
      tunerDescOpt = if (!hasTuner) None else Some(parseTuner().get)
    } yield CaraPipeline(pipeline, evaluator, tunerDescOpt)
  }

  private[pipelineparser] def parsePipeline(): Try[Pipeline] = {
    for {
      content <- contentTry
      stagesDescriptions <- extractStages(content)
      caraStages <- parseStages(stagesDescriptions)
      sparkStages <- buildStages(caraStages)
      pipeline <- buildPipeline(sparkStages)
    } yield pipeline
  }

  private[pipelineparser] def parseEvaluator(): Try[Evaluator] = {
    for {
      content <- contentTry
      evaluatorName <- extractEvaluator(content)
      evaluator = mapEvaluator(evaluatorName)
    } yield evaluator
  }

  private[pipelineparser] def parseTuner(): Try[TuningStageDescription] = {
    for {
      content <- contentTry
      tunerDesc <- extractTuner(content)
      validatedTunerDesc = mapTuner(tunerDesc)
    } yield validatedTunerDesc
  }



  private[pipelineparser] def extractStages(fileContent: JsonNode): Try[List[CaraStageDescription]] = Try {
    val stagesList =
      fileContent.at(s"/CaraPipeline").iterator().asScala.toList.filter(_.has("stage"))

    stagesList.map{
      stageDesc =>
        val name = stageDesc.at("/stage").asText()

        val paramsMap =
          if (stageDesc.has("params")) {
            val paramsJson = stageDesc.at("/params")
            val paramList = paramsJson.iterator().asScala.toList
            val paramNames = paramList.flatMap{ r =>r.fieldNames().asScala.toList}

            val paramsZip = paramNames zip paramList
              paramsZip.map {
                paramTuple =>
                  val name = paramTuple._1
                  val value = paramTuple._2.at(s"/$name").asText()
                  (name, value)
              }.toMap
          } else {
            Map.empty[String, String]
          }

        CaraStageDescription(name, paramsMap)
    }
  }

  private[pipelineparser] def extractEvaluator(fileContent: JsonNode): Try[String] = Try {
    val stagesList = fileContent.at(s"/CaraPipeline").iterator().asScala.toList.filter(_.has("evaluator"))
    val evaluatorList = stagesList.map{ stageDesc =>stageDesc.at("/evaluator").asText()}

    evaluatorList.length match {
      case 1 => evaluatorList.head
      case _ =>
        throw new Exception("Error: You must define exactly one SparkML Evaluator")
    }
  }

  private[pipelineparser] def hasTuner(): Try[Boolean] =
    for {
      content <- contentTry
      hasTuner = content.at(s"/CaraPipeline").iterator().asScala.toList.filter(_.has("tuner")).nonEmpty
    } yield hasTuner

  private[pipelineparser] def extractTuner(fileContent: JsonNode): Try[TuningStageDescription] = {
    val tunersList = fileContent.at(s"/CaraPipeline").iterator().asScala.toList.filter(_.has("tuner"))

    tunersList.length match {
      case l if l == 1 =>
        val tunerJson = tunersList.head
        val tunerName = tunerJson.at("/tuner").textValue()

        val paramsJson = tunerJson.at("/params")
          val paramList = paramsJson.iterator().asScala.toList
          paramList.length match {
            case 1 =>
              val paramName = paramList.flatMap { r => r.fieldNames().asScala.toList }.head
              val paramValue = paramList.head.at(s"/$paramName").asText()

              Success(TuningStageDescription(tunerName, paramName, paramValue))
            case _ =>
              Failure(new IllegalArgumentException("Tuners must have exactly one param"))
          }
      case _ =>
        Failure(new IllegalArgumentException("Error: You must define exactly one SparkML Evaluator"))
    }
  }

  private[pipelineparser] def parseSingleStageMap(stageDescription: CaraStageDescription): Try[CaraStage[_]] = {
    mapStage(stageDescription)
  }

  private[pipelineparser] def parseStages(stagesDescriptionsList: List[CaraStageDescription]): Try[List[CaraStage[_]]] = {
    Try(stagesDescriptionsList.map(parseSingleStageMap(_).get))
  }

  private[pipelineparser] def buildStages(stagesList: List[CaraStage[_]]): Try[List[PipelineStage]] = {
    Try(stagesList.map(_.build().get))
  }

  private[pipelineparser] def buildPipeline(mlStages: List[PipelineStage]): Try[Pipeline] = {
    Try(new Pipeline().setStages(mlStages.toArray))
  }

}

object CaraParser {
  def apply(caraYaml: CaraYamlReader): CaraParser = new CaraParser(caraYaml)
}
