package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{HashingTF => fromSparkML}
import java.lang.reflect.InvocationTargetException
import java.lang.IllegalArgumentException

class HashingTFTest extends TestBase {
  "HashingTF build success" should "build new HashingTF from given parameters and return the same args as SparkML HashingTF" in {
    val CaraDsFeature=HashingTF(
      Map ("Binary"->"true",
        "InputCol"->"Input",
        "OutputCol" -> "Col10"
        ,"NumFeatures"->"2"
      ))
    val SparkFeature=new fromSparkML()
      .setBinary(true)
      .setInputCol("Input")
      .setNumFeatures(2)
      .setOutputCol("Col10")

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList

    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList
    CaraDsParams should contain theSameElementsAs  SparkParams

  }
  "HashingTF build failure" should "fail to build HashingTF with wrong parameters" in {
    println("Binary  parameter must be Boolean ")
    an [IllegalArgumentException] must be thrownBy HashingTF(
      Map ("Binary"->"OK",
        "InputCol"->"Input",
        "OutputCol" -> "Col10"
        ,"NumFeatures"->"2"
      )).build().get
    println("NumFeatures parameter must be > 0 ")
    an [IllegalArgumentException] must be thrownBy HashingTF(
      Map ("Binary"->"OK",
        "InputCol"->"Input",
        "OutputCol" -> "Col10"
        ,"NumFeatures"->"0"
      )).build().get
  }
}
