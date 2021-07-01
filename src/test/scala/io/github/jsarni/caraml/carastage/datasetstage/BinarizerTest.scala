package io.github.jsarni.caraml.carastage.datasetstage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{Binarizer => fromSparkML}

class BinarizerTest extends TestBase {

  "Binarizer build Success" should "build new binarizer with parametres given on the Map and be the same with SparkMl Binarizer" in {

    val CaraDsFeature = Binarizer(
      Map(
        "InputCol"->"Input",
        "InputCols"->"col1 , col2 ,col3, col4",
        "OutputCol" ->"Output",
        "Threshold" -> "10.0",
        "OutputCols" -> "Col10 , Col11 ,Col_12, Col_vector_1",
        "Thresholds" -> "10.0 , 12.0 , 13.0"
      )
    )

    val SparkFeature = new fromSparkML()
      .setInputCol("Input")
      .setInputCols(Array("col1" , "col2" ,"col3", "col4"))
      .setOutputCol("Output")
      .setOutputCols(Array("Col10" , "Col11" ,"Col_12", "Col_vector_1"))
      .setThreshold(10.0)
      .setThresholds(Array(10.0 , 12.0 , 13.0))

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "Binarizer build Failure" should "Throw NumberFormatException " in {

    an [NumberFormatException] should be thrownBy Binarizer(
      Map(
        "InputCol" -> "Input",
        "InputCols" -> "col1 , col2 ,col3, col4",
        "OutputCol" -> "Output",
        "Threshold" -> "wrong_param",
        "OutputCols" -> "Col10 , Col11 ,Col_12, Col_vector_1",
        "Thresholds" -> "10.0 , 12.0 , 13.0"
      )
    )
  }

}
