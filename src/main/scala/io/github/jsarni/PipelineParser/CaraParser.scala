package io.github.jsarni.PipelineParser

import com.fasterxml.jackson.databind.JsonNode
import io.github.jsarni.CaraStage.{CaraStage, CaraStageDescription, CaraStageMapper}
import io.github.jsarni.CaraYaml.CaraYaml
import org.apache.spark.ml.{Pipeline, PipelineStage}

import scala.collection.JavaConverters._
import scala.util.Try

class CaraParser(caraYaml: CaraYaml) extends ParserUtils with CaraStageMapper{

  val contentTry = caraYaml.loadFile()

  def parse(): Try[Pipeline] = {
    for {
      content <- contentTry
      stagesDescriptions = extractStages(content)
      caraStages = parseStages(stagesDescriptions)
      sparkStages = buildStages(caraStages)
      pipeline = buildPipeline(sparkStages)
    } yield pipeline
  }

  private[PipelineParser] def extractStages(fileContent: JsonNode): List[CaraStageDescription] = {
    val stagesList = fileContent.at(s"/CaraPipeline").iterator().asScala.toList
    val stages = stagesList.map{
      stageDesc =>
        val name = stageDesc.at("/stage").asText()

        val paramsMap =
          if (stageDesc.has("params")) {
            val paramsJson = stageDesc.at("/params")
            val paramList = paramsJson.iterator().asScala.toList
            val paramNames = paramList.flatMap{ r =>r.fieldNames().asScala.toList}

            val paramsZip = paramNames zip paramList
              paramsZip.map{
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
    stages
  }

  private[PipelineParser] def parseStage(stageDescription: CaraStageDescription): Try[Any] =
    for {
      stageClass <- Try(Class.forName(s"io.github.jsarni.CaraStage.ModelStage.${stageDescription.stageName}"))
      constructor <- getMapperConstructor(stageClass)
      caraStage = constructor.newInstance(stageDescription.params)
    } yield caraStage

  private[PipelineParser] def parseSingleStageMap(stageDescription: CaraStageDescription): CaraStage = {
    mapStage(stageDescription)
  }

  private[PipelineParser] def parseStages(stagesDescriptionsList: List[CaraStageDescription]): List[CaraStage] = {
    stagesDescriptionsList.map(parseSingleStageMap(_))
  }

  private[PipelineParser] def buildStages(stagesList: List[CaraStage]): List[PipelineStage] = {
    stagesList.map(_.build())
  }

  private[PipelineParser] def buildPipeline(mlStages: List[PipelineStage]) = {
    new Pipeline()
      .setStages(mlStages.toArray)
  }

}
