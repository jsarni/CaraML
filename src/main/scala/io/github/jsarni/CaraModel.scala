package io.github.jsarni

import io.github.jsarni.DatasetLoader.CaraLoader
import io.github.jsarni.PipelineParser.CaraParser
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.PipelineModel
import io.github.jsarni.CaraYaml.CaraYamlReader
import scala.util.{Success, Try}

final class CaraModel(yamlPath: String, datasetPath: String, format: String)(implicit spark: SparkSession) {

  val yaml = CaraYamlReader(yamlPath)
  val parser = CaraParser(yaml)
  val loader = CaraLoader(datasetPath, format)

  final val model = ???

  def train() = ???


}
