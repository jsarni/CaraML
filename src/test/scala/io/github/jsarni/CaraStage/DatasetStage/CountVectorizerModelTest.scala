package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{CountVectorizerModel => fromSparkML}
import java.lang.reflect.InvocationTargetException
import java.lang.IllegalArgumentException

class CountVectorizerModelTest extends TestBase {
  "CountVectorizerModel build success" should "build new CountVectorizerModel from given parameters and return the same args as SparkML CountVectorizerModel" in {
    val CaraDsFeature=CountVectorizer(
      Map (
        "Binary"->"true",
        "InputCol"->"Input",
        "VocabSize"->"3",
        "OutputCol" -> "Col10"
        ,"MinDF" -> "1.0"
        ,"MinTF" -> "10.0"
        ,"MaxDF" -> "9.223372036854776E18"
        ,"Vocabulary"->"a, b, c"
      ))
    val SparkFeature=new fromSparkML(Array("a","b","c"))
      .setBinary(true)
      .setInputCol("Input")
      .setMinTF(10.0)
      .setOutputCol("Col10")

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    println(CaraDsFeature.build().get.extractParamMap().toSeq.map(f=>(f.param.toString.split("__")(1),f.value)).sortBy(_._1))
    println(SparkFeature.extractParamMap().toSeq.map(f=>(f.param.toString.split("__")(1),f.value)).sortBy(_._1))
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList
    CaraDsParams should contain theSameElementsAs  SparkParams

  }
  "CountVectorizer build failure" should "fail to build CountVectorizer with wrong parameters" in {
    println("Binary  parameter must be Boolean ")
    an [IllegalArgumentException] must be thrownBy CountVectorizer(
      Map ("Binary"->"OK",
        "InputCol"->"Input",
        "MaxDF"->"15.0",
        "MinDF" ->"8.0",
        "OutputCol" -> "Col10"
        ,"MinTF" -> "10.0"
        ,"VocabSize" -> "4"
      )).build().get
  }
}
