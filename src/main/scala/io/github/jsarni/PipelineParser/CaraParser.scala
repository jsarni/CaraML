package io.github.jsarni.PipelineParser

import java.io.{File, FileInputStream, FileReader}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import io.github.jsarni.CaraStage.CaraStage
import io.github.jsarni.CaraYaml.CaraYaml
import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe._
import scala.util.Try

final class CaraParser(caraYaml: CaraYaml) {

  private[PipelineParser] def loadFile(): Try[JsonNode] =
  for {
    ios <- Try(new FileInputStream(new File(caraYaml.path)))
    yaml = new Yaml()
    mapper = new ObjectMapper().registerModules(DefaultScalaModule)
    yamlObj = yaml.loadAs(ios, classOf[Any])
    jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlObj) // Formats YAML to a pretty printed JSON string - easy to read
    jsonObj = mapper.readTree(jsonString)
  } yield jsonObj

  private[PipelineParser] def extractStages(fileContent: JsonNode): List[CaraStage]  = {
    val stagesList = fileContent.at(caraYaml.header).iterator().asScala.toList
    val stages = stagesList.map{
      stageDesc =>
        val name = stageDesc.at("/stage").asText()

        val paramsMap =
          if (stageDesc.has("params")) {
            val paramsJson = stageDesc.at("/params")
            val paramList = paramsJson.iterator().asScala.toList
            val paramNames = paramList.flatMap{ r =>r.fieldNames().asScala.toList}

            val paramsZip = paramNames zip paramList
            Some(
              paramsZip.map{
                (paramTuple) =>
                  val name = paramTuple._1
                  val value = paramTuple._2.at(s"/$name").asText()
                  (name, value)
              }.toMap
            )
          } else {
            None
          }

        CaraStage(name, paramsMap)
    }
    stages
  }
}
