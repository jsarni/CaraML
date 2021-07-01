package io.github.jsarni



import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.PipelineParser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, GBTClassificationModel, LogisticRegressionModel, NaiveBayes, NaiveBayesModel, RandomForestClassificationModel}
import org.apache.spark.mllib.clustering.{KMeansModel, LDAModel}
import org.apache.spark.ml.regression.LinearRegressionModel
import org.apache.spark.ml.{Pipeline, PipelineModel, Transformer, classification}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder, TrainValidationSplit}

import scala.util.Try
import scala.util.control.Breaks._
import java.io._
import java.nio._
import java.nio.file.{Files, Paths}
import java.sql.Timestamp
import java.time.Instant
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


import org.apache.spark.ml.clustering.KMeans

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


  def writeHTMLFile(filename: String, ArrayStages: Array[mutable.SortedMap[String,Any]]): Unit = {

    //Save the Logo Img to the directory
    val logoIMG  = new File(filename + s"/caraML_logo_200x100.png")
    val logoRead = ImageIO.read(new File("src/main/resources/caraML_logo_200x100.png"))
    ImageIO.write(logoRead, "png", logoIMG)

    //Create the file
    val rapportDate: String= Timestamp.from(Instant.now()).toString.replace(" ","-")
    val fileHTML = new File(filename + s"/ModelMetrics_$rapportDate.html")
    val fileBufferWriter = new BufferedWriter(new FileWriter(fileHTML))

    // Load HTML Component
    val headHTML = new String(Files.readAllBytes(Paths.get("src/main/resources/header.txt")))
    val bodyPartOne = new String(Files.readAllBytes(Paths.get("src/main/resources/body_part1.txt")))
    val bodyPartTwo = new String(Files.readAllBytes(Paths.get("src/main/resources/body_part2.txt")))

    val firstPart ="<tr style=\"height: 18px;\">\n <th scope=\"row\"></th>\n <td>"
    val secondPart = "</td>\n<td>"

    // Write HTML Skeleton
    fileBufferWriter.write(headHTML)

      for (lines <- ArrayStages) {

        // Inject Model Metrics
        val modelName = lines.map(line => if (line._1 == "Model Name") line._2 else new String(""))
          .toList
          .filter(p => p.asInstanceOf[String].length > 0)
          .mkString

        // If the Stage dont have Metrics : print only the Stage Name
        if (modelName.endsWith("display")) {
          fileBufferWriter.write(bodyPartOne + s"$modelName </p> </div> </div>\n")

        }
        else{

          fileBufferWriter.write(bodyPartOne + s"$modelName </p> </div> </div> \n" + bodyPartTwo)

          for ((field, value) <- lines) {

            fileBufferWriter.write(s"$firstPart $field $secondPart $value </td> </tr> \n")
        }
          fileBufferWriter.write("</tbody>\n </table>\n </div>\n")
        }
      }
    fileBufferWriter.write("</body>\n</html>")
    fileBufferWriter.close()
  }

  private def generateReport(model: PipelineModel) : Try[Unit] = Try{

    val StageMetrics = model.stages.map(stage => getStageMetrics(stage).get )
    writeHTMLFile(savePath ,StageMetrics)

  }
  private def getStageMetrics(stage: Transformer) : Try[mutable.SortedMap[String,Any]] =Try {

    val stagesModel=stage
    val rapportDate: String= Timestamp.from(Instant.now()).toString.replace(" ","-")

    stagesModel match {

      case m: DecisionTreeClassificationModel => {

        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
                              "Model Name"                    -> modelName,
                              "Raport Date Generation"        -> rapportDate,
                              "Feature Importances"           -> m.featureImportances.toArray.toList.mkString,
                              "Number of Features"            -> m.numFeatures,
                              "features Column"               -> m.featuresCol.name
        )

      }

      case m: LogisticRegressionModel => {

        val summ=m.summary
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
                              "Model Name"                    -> modelName,
                              "Report Date Generation"        -> rapportDate,
                              "Accuracy"                      -> summ.accuracy,
                              "Features Columns"              -> summ.featuresCol,
                              "FMeasure By Label"             -> summ.fMeasureByLabel.toList.mkString(" \n"),
                              "False Positive Rate By Label"  -> summ.falsePositiveRateByLabel.toList.mkString(" \n"),
                              "Label Column"                  -> summ.labelCol,
                              "Labels"                        -> summ.labels.toList.mkString(" \n"),
                              "Objective History"             -> summ.objectiveHistory.toList.mkString(" \n"),
                              "Probability Column"            -> summ.probabilityCol.mkString,
                              "True Positive Rate By Label"   -> summ.truePositiveRateByLabel.toList.mkString(" \n"),
                              "Total Iterations"              -> summ.totalIterations,
                              "Prediction Column"             -> summ.predictionCol,
                              "Precision By Label"            -> summ.precisionByLabel.toList.mkString(" \n"),
                              "Recall By  Label"              -> summ.recallByLabel.toList.mkString(" \n"),
                              "Weighted Recall"               -> summ.weightedRecall,
                              "Weighted True Positive Rate"   -> summ.weightedTruePositiveRate,
                              "Weight Column"                 -> summ.weightCol,
                              "Weighted False Positive Rate"  -> summ.weightedFalsePositiveRate,
                              "Weighted FMeasure"             -> summ.weightedFMeasure
        )

      }

      case m: LinearRegressionModel   => {

        val summ = m.summary
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
                              "Model Name"                    -> modelName,
                              "Report Date Generation"        -> rapportDate,
                              "Objective History"             -> summ.objectiveHistory.toList.mkString,
                              "Total Iterations"              -> summ.totalIterations,
                              "Coefficient Standard Errors"   -> summ.coefficientStandardErrors.toList.mkString,
                              "Degrees Of Freedom"            -> summ.degreesOfFreedom,
                              "Deviance Residuals"            -> summ.devianceResiduals.toList.mkString,
                              "Explained Variance"            -> summ.explainedVariance,
                              "Features Column"               -> summ.featuresCol,
                              "Label Column"                  -> summ.labelCol,
                              "Mean Absolute Error"           -> summ.meanAbsoluteError,
                              "Mean Squared Error"            -> summ.meanSquaredError,
                              "Number of Instances"           -> summ.numInstances,
                              "Prediction Column"             -> summ.predictionCol,
                              "PValues"                       -> summ.pValues.toList.mkString,
                              "R2"                            -> summ.r2,
                              "R2adj"                         -> summ.r2adj,
                              "Root Mean Squared Error"       -> summ.rootMeanSquaredError,
                              "TValues"                       -> summ.tValues.toList.mkString
        )

      }

      case m: GBTClassificationModel   => {

        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Raport Date Generation"        -> rapportDate,
          "Feature Importances"           -> m.featureImportances.toArray.toList.mkString,
          "Number of Features"            -> m.numFeatures,
          "Features Column"               -> m.featuresCol.name,
          "Number of Trees"               -> m.getNumTrees
        )


      }

      case m:RandomForestClassificationModel => {

        val summ = m.summary
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Accuracy"                      -> summ.accuracy,
          "Weighted True Positive Rate"   -> summ.weightedTruePositiveRate,
          "Total Iterations"              -> summ.totalIterations,
          "Objective History"             -> summ.objectiveHistory.toList.mkString,
          "Recall By Label"               -> summ.recallByLabel.toList.mkString,
          "Weighted Recall"               -> summ.weightedRecall,
          "Precision By Label"            -> summ.precisionByLabel.toList.mkString,
          "Labels"                        -> summ.labels.toList.mkString,
          "Label Column"                  -> summ.labelCol,
          "Prediction Column"             -> summ.predictionCol,
          "False Positive Rate By Label"  -> summ.falsePositiveRateByLabel.toList.mkString,
          "Weight Column"                 -> summ.weightCol,
          "FMeasure By Label"             -> summ.fMeasureByLabel.toList.mkString,
          "Weighted FMeasure"             -> summ.weightedFMeasure,
          "Weighted Precision"            -> summ.weightedPrecision
        )
      }

      case m: KMeansModel => {
        val km= m.asInstanceOf[KMeansModel]

        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate)

      }
      case m : LDAModel  => {
        val summ = m
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
        "Model Name"                    -> modelName,
        "Report Date Generation"        -> rapportDate,
        "k"                             ->  m.k,
        )

      }
      case m : NaiveBayesModel => {

        val summ = m
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Pi"                            -> m.pi.toArray.toList.mkString,
          "Sigma"                         -> m.sigma.toArray.toList.mkString,
          "Theta"                         -> m.theta.toArray.toList.mkString)

      }
      case m => {
        val modelName : String= m.getClass.getName.split('.').last.replace("Model","")
        mutable.SortedMap("Model Name" -> s"$modelName : This Stage has no Metrics to display")
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
