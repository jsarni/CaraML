package io.github.jsarni

import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.DatasetLoader.CaraLoader
import io.github.jsarni.PipelineParser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.util.Try

final class CaraModel(yamlPath: String, datasetPath: String, format: String, savePath: String)(implicit spark: SparkSession) {

  val yaml = CaraYamlReader(yamlPath)
  val parser = CaraParser(yaml)
  val loader = CaraLoader(datasetPath, format)

  def run(): Try[Unit] = for {
    caraPipeline <- parser.build()
    sparkPipeline <- generateModel(caraPipeline)
    dataset <- loader.load()
    fittedModel <- train(sparkPipeline, dataset)
    _ <- generateReport(fittedModel)
    _ <- save(fittedModel)
  } yield ()

  def generateReport(model: PipelineModel) : Try[Unit] = ???

  private def generateModel(caraPipeline: CaraPipeline): Try[Pipeline] = ???

  private def train(pipeline: Pipeline, dataset: Dataset[_]): Try[PipelineModel] = Try {
    pipeline.fit(dataset)
  }

  private def save(model: PipelineModel) : Try[Unit] = Try {
    model.write.save(savePath)
  }


}
