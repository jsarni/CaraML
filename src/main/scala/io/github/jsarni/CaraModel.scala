package io.github.jsarni



import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.PipelineParser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.regression.LinearRegressionModel
import org.apache.spark.ml.{Pipeline, PipelineModel, classification}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder, TrainValidationSplit}

import scala.util.Try
import java.io._
import java.sql.Timestamp
import java.time.Instant

import scala.collection.mutable

final class CaraModel(yamlPath: String, dataset: Dataset[_], savePath: String)(implicit spark: SparkSession) {

  val yaml = CaraYamlReader(yamlPath)
  val parser = CaraParser(yaml)

  def run(): Try[Unit] = for {
    caraPipeline <- parser.build()
    sparkPipeline <- generateModel(caraPipeline)
    fittedModel <- train(sparkPipeline, dataset)
//    _ <- generateReport(fittedModel)
    _ <- save(fittedModel)
  } yield ()


  def writeFile(filename: String, lines: mutable.SortedMap[String,Any]): Unit = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    for ((field,value) <- lines) {
      println(s"$field : $value")
      bw.write(s"$field ; $value")
      bw.write("\n")
    }
    bw.close()
  }
  private def generateReport(model: PipelineModel) : Try[Unit] =Try {
    val stagesModel=model.stages.last
    val rapportDate: String= Timestamp.from(Instant.now()).toString.replace(" ","-")
    stagesModel match {
////      case m: LogisticRegressionModel => Map("Accuracy"                 ->m.summary.accuracy
//                                             ,"featuresCol"             ->m.summary.featuresCol,
//                                            "fMeasureByLabel"           ->m.summary.fMeasureByLabel,
//                                            "falsePositiveRateByLabel"  ->m.summary.falsePositiveRateByLabel,
//                                            "labelCol"                  ->m.summary.labelCol,
//                                            "labels"                    ->m.summary.labels,
//                                            "objectiveHistory"          ->m.summary.objectiveHistory,
//                                            "probabilityCol"            ->m.summary.probabilityCol,
//                                            "truePositiveRateByLabel"   ->m.summary.truePositiveRateByLabel,
//                                            "weightCol"                 ->m.summary.weightCol,
//                                            "weightedFalsePositiveRate" ->m.summary.weightedFalsePositiveRate,
//                                            "totalIterations"           ->m.summary.totalIterations,
//                                            "weightedFMeasure"          ->m.summary.weightedFMeasure
//                                          )
//

      case m: LogisticRegressionModel => {

        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        val modelMetrics= mutable.SortedMap(
                              "Model Name"                    ->modelName,
                              "Report Date Generation"        -> rapportDate,
                              "Accuracy"                      -> m.summary.accuracy,
                              "features Col"                  -> m.summary.featuresCol,
                              "fMeasure By Label"             -> m.summary.fMeasureByLabel.toList,
                              "false Positive Rate By Label"  -> m.summary.falsePositiveRateByLabel.toList,
                              "labelCol"                      -> m.summary.labelCol,
                              "labels"                        -> m.summary.labels.toList,
                              "objective History"             -> m.summary.objectiveHistory.toList,
                              "probability Col"               -> m.summary.probabilityCol,
                              "true Positive Rate By Label"   -> m.summary.truePositiveRateByLabel.toList,
                              "total Iterations"              -> m.summary.totalIterations,
                              "prediction Col"                -> m.summary.predictionCol,
                              "precision By Label"            -> m.summary.precisionByLabel.toList,
                              "recall By  Label"              -> m.summary.recallByLabel.toList,
                              "weighted Recall"               -> m.summary.weightedRecall,
                              "weighted True Positive Rate"   -> m.summary.weightedTruePositiveRate,
                              "weight Col"                    -> m.summary.weightCol,
                              "weighted False Positive Rate"  -> m.summary.weightedFalsePositiveRate,
                              "weighted FMeasure"             -> m.summary.weightedFMeasure
                          )
        writeFile(s"/home/aghylassai/Bureau/Spark/Arichtecture_Distribuees/$modelName$rapportDate.txt",modelMetrics)
      }
//      case m: LinearRegressionModel   => Map("objectiveHistory"->m.summary.objectiveHistory)
//      case m: LinearRegressionModel   => val fields=m.summary.getClass.getDeclaredFields

    }
  }

  def evaluate(dataset: Dataset[_]): Dataset[_] = {
    val model = PipelineModel.load(savePath)
    model.transform(dataset)
  }

  private def generateModel(caraPipeline: CaraPipeline) : Try[Pipeline] = Try {
    val pipeline = caraPipeline.pipeline
    val evaluator = caraPipeline.evaluator
    val tuningStage = caraPipeline.tuner.tuningStage
    val methodeName = "set" + caraPipeline.tuner.paramName
    val model = tuningStage match  {
      case "CrossValidator" => {
        val paramValue = caraPipeline.tuner.paramValue.toInt
        val crossValidatorModel = new CrossValidator()
          .setEstimator(pipeline)
          .setEvaluator(evaluator)
          .setEstimatorParamMaps(new ParamGridBuilder().build())
          .setParallelism(2)

        crossValidatorModel.getClass.getMethod(methodeName, paramValue.getClass )
          .invoke(crossValidatorModel,paramValue.asInstanceOf[java.lang.Integer])

        new Pipeline().setStages(Array(crossValidatorModel))
      }
      case "TrainValidationSplit" => {
        val paramValue = caraPipeline.tuner.paramValue.toDouble
        val validationSplitModel = new TrainValidationSplit()
          .setEstimator(pipeline)
          .setEvaluator(evaluator)
          .setEstimatorParamMaps(new ParamGridBuilder().build())
          .setParallelism(2)

        validationSplitModel.getClass.getMethod(methodeName, paramValue.getClass )
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
