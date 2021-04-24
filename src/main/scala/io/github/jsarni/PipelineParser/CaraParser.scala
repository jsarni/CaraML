package io.github.jsarni.PipelineParser

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.github.jsarni.CaraStage.{CaraStage, CaraStageDescription, CaraStageMapper}
import io.github.jsarni.CaraYaml.{CaraYaml, FileTypes}
import org.yaml.snakeyaml.Yaml

import java.io.{File, FileInputStream}
import scala.collection.JavaConverters._
import scala.util.Try

class CaraParser[T <: CaraStage](caraYaml: CaraYaml) extends ParserUtils with CaraStageMapper{

  private[PipelineParser] def loadFile(): Try[JsonNode] =
  for {
    ios <- Try(new FileInputStream(new File(caraYaml.filePath)))
    yaml = new Yaml()
    mapper = new ObjectMapper().registerModules(DefaultScalaModule)
    yamlObj = yaml.loadAs(ios, classOf[Any])
    jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlObj) // Formats YAML to a pretty printed JSON string - easy to read
    jsonObj = mapper.readTree(jsonString)
  } yield jsonObj

  private[PipelineParser] def extractStages(fileContent: JsonNode): List[CaraStageDescription]  = {
    val stagesList = fileContent.at(s"/${caraYaml.fileType}").iterator().asScala.toList
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

  def parseStage(stageDescription: CaraStageDescription): Try[Any] =
    for {
      stageClass <- Try(Class.forName(s"io.github.jsarni.CaraStage.ModelStage.${stageDescription.stageName}"))
      constructor <- getMapperConstructor(stageClass)
      caraStage = constructor.newInstance(stageDescription.params)
    } yield caraStage

  def parseStageMap(stageDescription: CaraStageDescription): CaraStage = {
    caraYaml.fileType match {
      case FileTypes.MODEL_FILE =>
        mapModelStage(stageDescription)
      case FileTypes.DATASET_FILE =>
        mapDatasetStage(stageDescription)
    }
  }

  def parseAllStages(stagesDescriptionsList: List[CaraStageDescription]): List[CaraStage] = {
    stagesDescriptionsList.map(parseStageMap(_))
  }


}
