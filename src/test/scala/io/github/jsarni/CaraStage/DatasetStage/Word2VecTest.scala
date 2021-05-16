package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{Word2Vec => fromSparkML}
import java.lang.reflect.InvocationTargetException
import java.lang.IllegalArgumentException

class Word2VecTest extends TestBase {
  "Word2Vec build Success" should "build new Word2Vec from given parameters and return the same args as SparkML Word2Vec" in {
    val CaraDsFeature=Word2Vec(
      Map ("InputCol"->"Input",
        "MaxIter"->"15",
        "MaxSentenceLength" ->"8",
        "NumPartitions"->"5",
        "OutputCol" -> "Col10"
        ,"StepSize" -> "10.0"
        ,"VectorSize" -> "4"
        ,"MinCount"->"3"
        ,"Seed" -> "10"
      ))
    val SparkFeature=new fromSparkML()
      .setInputCol("Input")
      .setMaxIter(15)
      .setMaxSentenceLength(8)
      .setNumPartitions(5)
      .setOutputCol("Col10")
      .setStepSize(10.0)
      .setVectorSize(4)
      .setMinCount(3)
      .setSeed(10)
    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList
    CaraDsParams should contain theSameElementsAs  SparkParams

  }
  "Word2Vec build Failure" should "fail to build Word2Vec with wrong parameters" in {
    println("stepSize  parameter must be >0")
    an [InvocationTargetException] must be thrownBy Word2Vec(
      Map ("InputCol"->"Input",
        "MaxIter"->"15",
        "MaxSentenceLength" ->"8",
        "NumPartitions"->"5",
        "OutputCol" -> "Col10"
        ,"StepSize" -> "0"
        ,"VectorSize" -> "4"
        ,"MinCount"->"3"
        ,"Seed" -> "10"
      )).build().get

  println("MaxIter  parameter must be >=0")
  an [InvocationTargetException] must be thrownBy Word2Vec(
    Map ("InputCol"->"Input",
      "MaxIter"->"-15",
      "MaxSentenceLength" ->"8",
      "NumPartitions"->"5",
      "OutputCol" -> "Col10"
      ,"StepSize" -> "0"
      ,"VectorSize" -> "4"
      ,"MinCount"->"3"
      ,"Seed" -> "10"
    )).build().get
}

}
