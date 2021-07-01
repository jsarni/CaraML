package io.github.jsarni.caraml

import io.github.jsarni.caraml.carayaml.CaraYamlReader
import io.github.jsarni.caraml.pipelineparser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.Dataset

import scala.util.Try

final class CaraModel(yamlPath: String, dataset: Dataset[_], savePath: String, overwrite: Boolean = true) {

  val yaml = CaraYamlReader(yamlPath)
  val parser = CaraParser(yaml)

  def run(): Try[Unit] = for {
    caraPipeline <- parser.build()
    sparkPipeline <- generateModel(caraPipeline)
    fittedModel <- train(sparkPipeline, dataset)
//    _ <- generateReport(fittedModel)
    _ <- save(fittedModel)
  } yield ()

//  def generateReport(model: PipelineModel) : Try[Unit] = ???

  def evaluate(dataset: Dataset[_]): Dataset[_] = {
    val model = PipelineModel.load(savePath)
    model.transform(dataset)
  }

  private def generateModel(caraPipeline: CaraPipeline) : Try[Pipeline] = Try {
    val pipeline = caraPipeline.pipeline
    val evaluator = caraPipeline.evaluator

    caraPipeline.tuner match {
      case Some(tuner) =>
        val methodeName = "set" + tuner.paramName
        tuner.tuningStage match  {
          case "CrossValidator" =>
            val paramValue = tuner.paramValue.toInt
            val crossValidatorModel = new CrossValidator()
              .setEstimator(pipeline)
              .setEvaluator(evaluator)
              .setEstimatorParamMaps(new ParamGridBuilder().build())
              .setParallelism(2)

            crossValidatorModel.getClass.getMethod(methodeName, paramValue.getClass)
              .invoke(crossValidatorModel,paramValue.asInstanceOf[java.lang.Integer])

            new Pipeline().setStages(Array(crossValidatorModel))

          case "TrainValidationSplit" =>
            val paramValue = tuner.paramValue.toDouble
            val validationSplitModel = new TrainValidationSplit()
              .setEstimator(pipeline)
              .setEvaluator(evaluator)
              .setEstimatorParamMaps(new ParamGridBuilder().build())
              .setParallelism(2)

            validationSplitModel.getClass.getMethod(methodeName, paramValue.getClass)
              .invoke(validationSplitModel,paramValue.asInstanceOf[java.lang.Double])

            new Pipeline().setStages(Array(validationSplitModel))
        }
      case None =>
        pipeline
    }

  }

  private def train(pipeline:  Pipeline , dataset: Dataset[_]): Try[PipelineModel] = Try {
    pipeline.fit(dataset)
  }

  private def save(model: PipelineModel) : Try[Unit] = Try {
    if (overwrite)
      model.write.overwrite().save(savePath)
    else
      model.write.save(savePath)
  }

}
