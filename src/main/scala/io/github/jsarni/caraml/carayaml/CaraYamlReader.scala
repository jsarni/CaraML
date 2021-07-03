package io.github.jsarni.caraml.carayaml

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.yaml.snakeyaml.Yaml

import java.io.{File, FileInputStream}
import scala.util.Try

final case class CaraYamlReader(yamlPath: String){

  def loadFile(): Try[JsonNode] = for {
    ios <- Try(new FileInputStream(new File(yamlPath)))
    yaml = new Yaml()
    mapper = new ObjectMapper().registerModules(DefaultScalaModule)
    yamlObj = yaml.loadAs(ios, classOf[Any])
    jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlObj)
    jsonObj = mapper.readTree(jsonString)
  } yield jsonObj

}
