package io.github.jsarni

import io.github.jsarni.CaraStage.ModelStage.LogisticRegression
import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.DatasetLoader.CaraLoader
import io.github.jsarni.PipelineParser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.regression.LinearRegressionModel
import org.apache.spark.ml.{Pipeline, PipelineModel, classification}
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

  private def generateReport(model: PipelineModel) : Try[Unit] =Try {
    val stagesModel=model.stages.last
    val summaries  =stagesModel match {
      case m: LogisticRegressionModel => m.summary
      case m: LinearRegressionModel   => m.summary
      case _ => _
    }
    summaries

  }

  private def generateModel(caraPipeline: CaraPipeline): Try[Pipeline] = ???

  private def train(pipeline: Pipeline, dataset: Dataset[_]): Try[PipelineModel] = Try {
    pipeline.fit(dataset)
  }

  private def save(model: PipelineModel) : Try[Unit] = Try {
    model.write.save(savePath)
  }


}
