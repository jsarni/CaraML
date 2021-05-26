package io.github.jsarni

import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.DatasetLoader.CaraLoader
import io.github.jsarni.PipelineParser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.ml.tuning.{CrossValidator, TrainValidationSplit}

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

  
  private def generateModel(caraPipeline: CaraPipeline) : Try[Pipeline] = Try {
    val pipeline = caraPipeline.pipeline
    val evaluator = caraPipeline.evaluator
    val tuningStage = caraPipeline.tuner.tuningStage
    val methodeName = "set" + caraPipeline.tuner.paramName
    val model = tuningStage match  {
      case "CrossValidator" => {
        val paramValue = caraPipeline.tuner.paramValue
        val crossValidatorModel = new CrossValidator()
          .setEstimator(pipeline)
          .setEvaluator(evaluator)
          .setParallelism(2)

        crossValidatorModel.getClass.getMethod(methodeName, Int.getClass )
          .invoke(crossValidatorModel,paramValue.asInstanceOf[java.lang.Integer])
        
        new Pipeline().setStages(Array(crossValidatorModel))
      }
      case "TrainValidationSplit" => {
        val paramValue = caraPipeline.tuner.paramValue
        val validationSplitModel = new TrainValidationSplit()
          .setEstimator(pipeline)
          .setEvaluator(evaluator)
          .setParallelism(2)

        validationSplitModel.getClass.getMethod(methodeName, Double.getClass )
          .invoke(validationSplitModel,paramValue.asInstanceOf[java.lang.Double])

        new Pipeline().setStages(Array(validationSplitModel))
      }
    }
    model
  }

  private def train(pipeline:  Pipeline , dataset: Dataset[_]): Try[PipelineModel] = Try {
    pipeline.fit(dataset)
  }

  private def save(model: PipelineModel) : Try[Unit] = Try {
    model.write.save(savePath)
  }


}
