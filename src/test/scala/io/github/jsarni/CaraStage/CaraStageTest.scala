package io.github.jsarni.CaraStage

import io.github.jsarni.TestBase
import io.github.jsarni.CaraStage.ModelStage.{CaraModel, LogisticRegression}
import org.apache.spark.ml.classification.{LogisticRegression => log}
//
//import java.io.FileNotFoundException
//import scala.io.Source
//import scala.util.Try

class CaraStageTest extends TestBase {

  "GetMethode" should("Return the appropriate methode by it's")


  "LogisticRegression" should("Create an lr model and set all parameters with there args values") in {
//    val basicParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial")
    val allParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial" ,"FeaturesCol" -> "FeatureColname"
      , "FitIntercept" -> "True", "PredictionCol" -> "Age", "ProbabilityCol" -> "ProbaColname", "RawPredictionCol"-> "RawPredictColname"
      , "Standardization" -> "True" , "Tol" -> "0.13", "WeightCol" -> "WeightColname")
    val r = new log()
//    println(r.getClass.getMethod("GetMaxIter", Int.getClass ))

//    val lr = LogisticRegression(basicParams)
    val lr1 = LogisticRegression( allParams)
//    print(lr1.asInstanceOf[log])
//    lr.build()
    val model =  lr1.build()
    model

  }
}
