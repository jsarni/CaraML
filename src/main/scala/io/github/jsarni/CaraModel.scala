package io.github.jsarni

import io.github.jsarni.CaraYaml.CaraYaml
import io.github.jsarni.DatasetLoader.CaraLoader
import io.github.jsarni.PipelineParser.CaraParser
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.PipelineModel
import scala.util.Try

final class CaraModel(yamlPath: String, datasetPath: String, format: String)(implicit spark: SparkSession) {

  val yaml = CaraYaml(yamlPath)
  val parser = CaraParser(yaml)
  val loader = CaraLoader(datasetPath, format)


  private def train(): Try[PipelineModel] = {
    for {
      pipeline <- parser.parse()
      dataset <- loader.load()
      model = pipeline.fit(dataset)
    } yield model
  }


}
