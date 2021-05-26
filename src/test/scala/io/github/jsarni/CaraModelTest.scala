package io.github.jsarni
import io.github.jsarni.CaraStage.ModelStage.LogisticRegression
import io.github.jsarni.CaraStage.TuningStage.TuningStageDescription
import io.github.jsarni.PipelineParser.CaraPipeline
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, RegressionEvaluator}


class CaraModelTest extends TestBase {
  "generateModel" should "Return validation model with the right method and params" in {
    val params = Map(
      "MaxIter" -> "10",
      "RegParam" -> "0.3",
      "ElasticNetParam" -> "0.1",
      "Family" -> "multinomial",
      "FeaturesCol" -> "FeatureColname",
      "FitIntercept" -> "True",
      "PredictionCol" -> "Age",
      "ProbabilityCol" -> "ProbaColname",
      "RawPredictionCol"-> "RawPredictColname",
      "Standardization" -> "True",
      "Tol" -> "0.13",
      "WeightCol" -> "WeightColname"
    )
    val lr = LogisticRegression(params)
    val crossEvaluator = new BinaryClassificationEvaluator
    val crossTuner = TuningStageDescription("CrossValidator", "NumFolds", "2" )
    val splitEvaluator = new RegressionEvaluator
    val splitTuner = TuningStageDescription("TrainValidationSplit", "TrainRatio", "0.6" )

    lr.build().isSuccess shouldBe true

    val pipeline = new Pipeline()
      .setStages(Array(lr.build().get))

    val crossCaraPipeline = CaraPipeline(pipeline, crossEvaluator, crossTuner)
    PrivateMethod('generateModel) (crossCaraPipeline)

    val splitCaraPipeline = CaraPipeline(pipeline, splitEvaluator, splitTuner)
    PrivateMethod('generateModel) (splitCaraPipeline)
  }

}
