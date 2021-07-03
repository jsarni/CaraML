package io.github.jsarni

import io.github.jsarni.caraml.CaraModel
import io.github.jsarni.caraml.carastage.modelstage.LogisticRegression
import io.github.jsarni.caraml.carastage.tuningstage.TuningStageDescription
import io.github.jsarni.caraml.pipelineparser.CaraPipeline
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, RegressionEvaluator}
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.tuning.{CrossValidator, TrainValidationSplit}
import org.apache.spark.sql.SparkSession


import scala.util.Try

class CaraModelTest extends TestBase {
  "generateModel" should "Return validation model with the right method and params" in {
    val lr = new LinearRegression()
      .setMaxIter(10)

    val crossEvaluator = new BinaryClassificationEvaluator
    val crossTuner = TuningStageDescription("CrossValidator", "NumFolds", "2")
    val splitEvaluator = new RegressionEvaluator
    val splitTuner = TuningStageDescription("TrainValidationSplit", "TrainRatio", "0.6")

    implicit val spark: SparkSession =
      SparkSession.builder()
        .appName("CaraML")
        .master("local[1]")
        .getOrCreate()

    val caraModel = new CaraModel("YamlPath", spark.emptyDataFrame,  "savePath")
    val pipeline = new Pipeline().setStages(Array(lr))
    val crossCaraPipeline = CaraPipeline(pipeline, crossEvaluator, Some(crossTuner))
    val splitCaraPipeline = CaraPipeline(pipeline, splitEvaluator, Some(splitTuner))
    val method = PrivateMethod[Try[Pipeline]]('generateModel)

    val crossModel = caraModel.invokePrivate(method(crossCaraPipeline))
    val splitModel = caraModel.invokePrivate(method(splitCaraPipeline))

    crossModel.isSuccess shouldBe true
    crossModel.get.getStages.length shouldBe 1
    crossModel.get.getStages.head.isInstanceOf[CrossValidator] shouldBe true
    crossModel.get.getStages.head.asInstanceOf[CrossValidator].getNumFolds shouldBe 2

    splitModel.isSuccess shouldBe true
    splitModel.get.getStages.length shouldBe 1
    splitModel.get.getStages.head.isInstanceOf[TrainValidationSplit] shouldBe true
    splitModel.get.getStages.head.asInstanceOf[TrainValidationSplit].getTrainRatio shouldBe 0.6
  }
  "generateReport" should "return the Pipeline metrics" in {

    implicit val spark: SparkSession =
      SparkSession.builder()
        .appName("CaraML")
        .master("local[1]")
        .getOrCreate()

    val Data = spark.createDataFrame(
      Seq(
        (0.0, 0.21, 0.66),
        (0.0, 0.38, 0.78),
        (1.0, 0.55, 0.25),
        (1.0, 0.70, 0.10),
        (1.0, 0.91, 0.06),
        (0.0, 0.27, 0.70)
      )
    ).toDF("labels", "values1","values2")

    val cols = Array( "values1","values2")
    val assembler = new VectorAssembler()
      .setInputCols(cols)
      .setOutputCol("features")

    val indexer = new StringIndexer()
      .setInputCol("labels")
      .setOutputCol("label")

    val logi = LogisticRegression(Map("MaxIter"->"10")).build().get

    val pipeline = new Pipeline()
      .setStages(Array(assembler,indexer,logi))

    val method = PrivateMethod[Try[Unit]]('generateReport)
    val fitedPipeline = pipeline.fit(Data)

    val caraModel = new CaraModel("yaml_path", Data, "/model.cml")
    val reportModel = caraModel. invokePrivate(method(fitedPipeline))
    reportModel.isSuccess shouldBe true
  }


}
