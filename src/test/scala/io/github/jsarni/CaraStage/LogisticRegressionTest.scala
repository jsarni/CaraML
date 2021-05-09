package io.github.jsarni.CaraStage

import io.github.jsarni.TestBase
import io.github.jsarni.CaraStage.ModelStage.{CaraModel, LogisticRegression}
import org.apache.spark.ml.classification.{LogisticRegression => log}
//
//import java.io.FileNotFoundException
//import scala.io.Source
import scala.util.{Try,Success, Failure}

class LogisticRegressionTest extends TestBase {


  "build" should("Create an lr model and set all parameters with there args values") in {
    val basicParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial")
    val allParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial" ,"FeaturesCol" -> "FeatureColname"
      , "FitIntercept" -> "True", "PredictionCol" -> "Age", "ProbabilityCol" -> "ProbaColname", "RawPredictionCol"-> "RawPredictColname"
      , "Standardization" -> "True" , "Tol" -> "0.13", "WeightCol" -> "WeightColname")
    val lr = LogisticRegression( basicParams)
    val model = lr.build().asInstanceOf[log]

    model.getMaxIter shouldBe 10
    model.getRegParam shouldBe 0.3
    model.getElasticNetParam shouldBe 0.1
    model.getFamily shouldBe "multinomial"

  }
  "GetMethode" should("Return the appropriate methode by it's name") in {
    val allParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial" ,"FeaturesCol" -> "FeatureColname"
      , "FitIntercept" -> "True", "PredictionCol" -> "Age", "ProbabilityCol" -> "ProbaColname", "RawPredictionCol"-> "RawPredictColname"
      , "Standardization" -> "True" , "Tol" -> "0.13", "WeightCol" -> "WeightColname")
    val lg = new log()
    val lr = LogisticRegression( allParams)
//    val model =Success(lr.build()).asInstanceOf[log]
//    println(lr.GetMethode(lg,10,"MaxIter").getName)
  }
}

