package io.github.jsarni



import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.PipelineParser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, GBTClassificationModel, LogisticRegressionModel, RandomForestClassificationModel}
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
      case m: DecisionTreeClassificationModel => {

        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        val modelMetrics = mutable.SortedMap(
                              "Model Name"                    -> modelName,
                              "Raport Date Generation"        -> rapportDate,
                              "featureImportances"            -> m.featureImportances.toArray.toList,
                              "numFeatures"                   -> m.numFeatures,
                              "featuresCol"                   -> m.featuresCol.name)
        writeFile(savePath+ s"/ModelResults.txt",modelMetrics)
      }
      case m: LogisticRegressionModel => {
        val summ=m.summary
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        val modelMetrics= mutable.SortedMap(
                              "Model Name"                    -> modelName,
                              "Report Date Generation"        -> rapportDate,
                              "Accuracy"                      -> summ.accuracy,
                              "features Col"                  -> summ.featuresCol,
                              "fMeasure By Label"             -> summ.fMeasureByLabel.toList,
                              "false Positive Rate By Label"  -> summ.falsePositiveRateByLabel.toList,
                              "labelCol"                      -> summ.labelCol,
                              "labels"                        -> summ.labels.toList,
                              "objective History"             -> summ.objectiveHistory.toList,
                              "probability Col"               -> summ.probabilityCol,
                              "true Positive Rate By Label"   -> summ.truePositiveRateByLabel.toList,
                              "total Iterations"              -> summ.totalIterations,
                              "prediction Col"                -> summ.predictionCol,
                              "precision By Label"            -> summ.precisionByLabel.toList,
                              "recall By  Label"              -> summ.recallByLabel.toList,
                              "weighted Recall"               -> summ.weightedRecall,
                              "weighted True Positive Rate"   -> summ.weightedTruePositiveRate,
                              "weight Col"                    -> summ.weightCol,
                              "weighted False Positive Rate"  -> summ.weightedFalsePositiveRate,
                              "weighted FMeasure"             -> summ.weightedFMeasure
                          )
        writeFile(savePath+ s"/ModelResults.txt",modelMetrics)
      }
      case m: LinearRegressionModel   => {

        val summ = m.summary
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        val modelMetrics= mutable.SortedMap(
                              "Model Name"                    -> modelName,
                              "Report Date Generation"        -> rapportDate,
                              "objectiveHistory"              -> summ.objectiveHistory.toList,
                              "totalIterations"               -> summ.totalIterations,
                              "coefficientStandardErrors"     -> summ.coefficientStandardErrors.toList,
                              "degreesOfFreedom"              -> summ.degreesOfFreedom,
                              "devianceResiduals"             -> summ.devianceResiduals.toList,
                              "explainedVariance"             -> summ.explainedVariance,
                              "featuresCol"                   -> summ.featuresCol,
                              "labelCol"                      -> summ.labelCol,
                              "meanAbsoluteError"             -> summ.meanAbsoluteError,
                              "meanSquaredError"              -> summ.meanSquaredError,
                              "numInstances"                  -> summ.numInstances,
                              "predictionCol"                 -> summ.predictionCol,
                              "pValues"                       -> summ.pValues.toList,
                              "r2"                            -> summ.r2,
                              "r2adj"                         -> summ.r2adj,
                              "rootMeanSquaredError"          -> summ.rootMeanSquaredError,
                              "tValues"                       -> summ.tValues.toList)
        writeFile(savePath+ s"/ModelResults.txt",modelMetrics)
      }
      case m: GBTClassificationModel   => {
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        val modelMetrics = mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Raport Date Generation"        -> rapportDate,
          "featureImportances"            -> m.featureImportances.toArray.toList,
          ""                              -> m.numFeatures,
          ""                              -> m.featuresCol,
          ""                              -> m.getNumTrees,
          ""                              -> m.toString())
        writeFile(savePath+ s"/ModelResults.txt",modelMetrics)

      }
      case m:RandomForestClassificationModel => {
        val summ = m.summary
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        val modelMetrics= mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "accuracy"                      -> summ.accuracy,
          "weightedTruePositiveRate"      -> summ.weightedTruePositiveRate,
          "totalIterations"               -> summ.totalIterations,
          "objectiveHistory"              -> summ.objectiveHistory.toList,
          "recallByLabel"                 -> summ.recallByLabel.toList,
          "weightedRecall"                -> summ.weightedRecall,
          "precisionByLabel"              -> summ.precisionByLabel.toList,
          "labels"                        -> summ.labels.toList,
          "labelCol"                      -> summ.labelCol,
          "predictionCol"                 -> summ.predictionCol,
          "falsePositiveRateByLabel"      -> summ.falsePositiveRateByLabel.toList,
          "weightCol"                     -> summ.weightCol,
          "fMeasureByLabel"               -> summ.fMeasureByLabel.toList,
          "weightedFMeasure"              -> summ.weightedFMeasure,
          "weightedPrecision"             -> summ.weightedPrecision
        )
      }

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
