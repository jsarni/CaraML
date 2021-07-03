package io.github.jsarni.caraml

import io.github.jsarni.caraml.carayaml.CaraYamlReader
import io.github.jsarni.caraml.pipelineparser.{CaraParser, CaraPipeline}
import org.apache.spark.ml.tuning.{CrossValidator, CrossValidatorModel, ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, GBTClassificationModel, LogisticRegressionModel, NaiveBayes, NaiveBayesModel, RandomForestClassificationModel}
import org.apache.spark.mllib.clustering.{KMeansModel, LDAModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressionModel, GBTRegressionModel, LinearRegressionModel, RandomForestRegressionModel}
import org.apache.spark.ml.{Pipeline, PipelineModel, Transformer}
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.collection.mutable
import scala.util.Try
import scala.util.{Success, Try}
import java.io._
import java.nio.file.{Files, Paths}
import java.sql.Timestamp
import java.time.Instant

import javax.imageio.ImageIO

import scala.collection.mutable


final class CaraModel(yamlPath: String, dataset: Dataset[_], savePath: String, overwrite: Boolean = true) {

  val yaml = CaraYamlReader(yamlPath)
  val parser = CaraParser(yaml)

  def run(): Try[Unit] = for {
    caraPipeline <- parser.build()
    sparkPipeline <- generateModel(caraPipeline)
    fittedModel <- train(sparkPipeline, dataset)
    _ <- generateReport(fittedModel)
    _ <- save(fittedModel)
  } yield ()

  private def evaluateReport(model : PipelineModel, dataset: Dataset[_]): String = {
    // Get 10 first Values to show
    val eval = model.transform(dataset).limit(10)

    // Prepare the Html Table Skeleton
    val bluePart   = "<div id=\"Models and Metrics BLUE\" class=\" p-3 mb-2 bg-info text-white\">\n<p style=\"color: #FFFFFF; font-size: 25px;\"class=\"text-center\">Model Evaluation </p>\n</div>\n"
    val columnPart = "<table  class =\"table table-striped table-dark \" style=\" width: 1000px; margin: 0 auto;\">\n<thead class=\"thead-blue\" >\n<tr style=\"height: 18px;\">\n<th scope=\"col\"> </th>"
    val partOne    = "<th scope= \"col \" class= \"text-center \" style= \" font-size: 25px;\">"
    val partTwo    = "<td class=\" text-center \">"

    val hearderPart : String    = s"$bluePart$columnPart"
    val columnList : String     = eval.columns.map( col => s"$partOne ${col.capitalize} </th>\n" ).mkString
    val valuesPart : String     =  eval.collect().map { row =>
      List("<tr style=\"height: 18px;\">\n <th scope=\"row\"class=\"text-center\"></th>\n".mkString,
        row.toSeq.map( field => s"$partTwo $field </td>\n").mkString,
        "</tr>\n").mkString
    }.mkString

    val finalSkeleton : String  = s"$hearderPart $columnList </tr>\n</thead>\n<tbody>\n$valuesPart </tbody>\n </table> \n <p> <br>  </p>\n </body> \n </html>"

    finalSkeleton

  }

  private def writeHTMLFile(filename: String, ArrayStages: Array[mutable.SortedMap[String,Any]], evalModel : PipelineModel): Unit = {

    // Set Paths and File Name

    val reportDirectory = new File(filename.split('/').dropRight(1).mkString("/")+"/Report Model")
    if (reportDirectory.mkdirs() == false) {
      reportDirectory.listFiles().map(file => file.delete())
      reportDirectory.delete()
      reportDirectory.mkdirs()
    }

      //Save the Logo Img to the directory
      val logoIMG  = new File(reportDirectory + s"/caraML_logo_200x100.png")
      val logoRead = ImageIO.read(new File("src/main/resources/caraML_logo_200x100.png"))
      ImageIO.write(logoRead, "png", logoIMG)

      //Create the HTML file
      val fileHTML = new File(reportDirectory + s"/ModelMetrics.html")
      val fileBufferWriter = new BufferedWriter(new FileWriter(fileHTML))

      // Load HTML Component from existing Resources
      val headHTML = new String(Files.readAllBytes(Paths.get("src/main/resources/header.txt")))
      val bodyPartOne = new String(Files.readAllBytes(Paths.get("src/main/resources/body_part1.txt")))
      val bodyPartTwo = new String(Files.readAllBytes(Paths.get("src/main/resources/body_part2.txt")))

      val firstPart ="<tr style=\"height: 18px;\">\n <th scope=\"row\"></th>\n <td class=\"text-center\">"
      val secondPart = "</td>\n<td class=\"text-center\">"

      // Write HTML Skeleton
      fileBufferWriter.write(headHTML)

      for (lines <- ArrayStages) {

        // Get the Model Name
        val modelName = lines.map(line => if (line._1 == "Model Name") line._2 else new String(""))
          .toList
          .filter(p => p.asInstanceOf[String].length > 0)
          .mkString

        // If the Stage doesn't have Metrics : print only the Stage Name
        if (modelName.endsWith("display")) {
          fileBufferWriter.write(bodyPartOne + s"$modelName </p> </div> </div>\n")

        }
        else {
          // Inject Model Metrics
          fileBufferWriter.write(bodyPartOne + s"$modelName </p> </div> </div> \n" + bodyPartTwo)
          for ((field, value) <- lines) {

            fileBufferWriter.write(s"$firstPart $field $secondPart $value </td> </tr> \n")
          }

          fileBufferWriter.write("</tbody>\n </table>\n </div>\n <p> <br>  </p>\n")
        }
      }

      //Write the Evaluate Model Results
      val evaluationResults : String = evaluateReport(evalModel,dataset)
      fileBufferWriter.write(evaluationResults)

      fileBufferWriter.close()


  }

  private def getStageMetrics(stage: Transformer) : Try[mutable.SortedMap[String,Any]] =Try {

    val rapportDate: String= Timestamp.from(Instant.now()).toString.replace(" ","-")
    val modelName : String= stage.getClass.getName.split('.').last.replace("Model","")

    stage match {
      // Classification Models
      case m: DecisionTreeClassificationModel => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Raport Date Generation"        -> rapportDate,
          "Feature Importances"           -> m.featureImportances.toArray.toList.mkString,
          "Number of Features"            -> m.numFeatures,
          "features Column"               -> m.featuresCol.name
        )
      }

      case m: GBTClassificationModel   => {
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
      //Regression Models
      case m : DecisionTreeRegressionModel => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Feature Importances"           -> m.featureImportances.toArray.mkString("\n"),
          "Features Number"               -> m.numFeatures,
          "Features Columns"              -> m.featuresCol.name
        )
      }

      case m : RandomForestRegressionModel => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Features Column"               -> m.featuresCol,
          "Feature Importances"           -> m.featureImportances.toArray.mkString("\n"),
          "Features Number"               -> m.numFeatures
        )
      }

      case m : GBTRegressionModel => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Features Column"               -> m.featuresCol,
          "Feature Importances"           -> m.featureImportances.toArray.mkString("\n"),
          "Features Number"               -> m.numFeatures
        )
      }

      case m: LogisticRegressionModel => {
        val summ=m.summary
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

      //Clustering Models
      case m: KMeansModel => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Cluster Centers"               -> m.clusterCenters,
          "Distance Measure"              -> m.distanceMeasure
        )
      }

      case m : LDAModel  => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "k"                             ->  m.k
        )
      }

      case m : NaiveBayesModel => {
        mutable.SortedMap(
          "Model Name"                    -> modelName,
          "Report Date Generation"        -> rapportDate,
          "Pi"                            -> m.pi.toArray.toList.mkString,
          "Sigma"                         -> m.sigma.toArray.toList.mkString,
          "Theta"                         -> m.theta.toArray.toList.mkString
        )
      }
      //Default
      case m => {
        mutable.SortedMap("Model Name" -> s"$modelName : This Stage has no Metrics to display")
      }
    }
  }

  private def generateReport(model: PipelineModel) : Try[Unit] = Try{

    val StageMetrics = model.stages.map(stage => getStageMetrics(stage).get )
    writeHTMLFile(savePath ,StageMetrics,model)
  }

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
              .setCollectSubModels(true)

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
              .setCollectSubModels(true)

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
    if (overwrite) {
      model.write.overwrite().save(savePath)
    } else
      model.write.save(savePath)
  }

}
