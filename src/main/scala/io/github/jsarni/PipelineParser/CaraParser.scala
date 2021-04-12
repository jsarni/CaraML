package io.github.jsarni.PipelineParser

import java.io.{File, FileInputStream, FileReader}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import io.github.jsarni.PipelineParser.DatasetStage.DatasetStage
import org.yaml.snakeyaml.Yaml

import scala.reflect.runtime.universe._

final class CaraParser[T <: CaraStage](path: String) {

//  Try[List[CaraStage]]
  final def loadFile: JsonNode = {

    // Parsing the YAML file with SnakeYAML - since Jackson Parser does not have Anchors and reference support
    val ios = new FileInputStream(new File(path))
    val yaml = new Yaml()
    val mapper = new ObjectMapper().registerModules(DefaultScalaModule)
    val yamlObj = yaml.loadAs(ios, classOf[Any])

    // Converting the YAML to Jackson YAML - since it has more flexibility
    val jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlObj) // Formats YAML to a pretty printed JSON string - easy to read
    val jsonObj = mapper.readTree(jsonString)
    jsonObj
  }

  def getFileType[T <: CaraStage]()(implicit tt: TypeTag[T]): String = {
    if (typeOf[T] <:< typeOf[DatasetStage]) "/dataset" else "/model"
  }
  def parseStage[T <: CaraStage](): Unit = {
    val fileHeader = getFileType()
  }
}
