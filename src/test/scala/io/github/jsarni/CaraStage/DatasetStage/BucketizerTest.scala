package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{Bucketizer => fromSparkML}

import java.lang.reflect.InvocationTargetException

class BucketizerTest extends TestBase {

  "Bucketizer build Success" should
    "build new Bucketizer with parametres given on the Map and be the same with SparkMl Bucketizer" in {

    val CaraDsFeature=Bucketizer(
      Map(
        "HandleInvalid" -> "skip",
        "InputCol" -> "Input",
        "InputCols" -> "col1 , col2 ,col3, col4",
        "OutputCol" -> "Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1",
        "Splits" -> "-0.5,0.0,0.5",
        "SplitsArray" -> "1.0 , 4.0 , 8.0"
      )
    )

    val SparkFeature = new fromSparkML()
      .setHandleInvalid("skip")
      .setInputCol("Input")
      .setInputCols(Array("col1" , "col2" ,"col3", "col4"))
      .setOutputCol("Output")
      .setOutputCols(Array("Col10" , "Col11" ,"Col_12", "Col_vector_1"))
      .setSplits(Array(-0.5,0.0,0.5))
      .setSplitsArray(Array(Array(1.0 , 4.0 , 8.0)))

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "Bucketizer build Failure" should "fail to build new Bucketizer when wrong parameter is set" in {

    an [InvocationTargetException] must be thrownBy Bucketizer(
      Map(
        "HandleInvalid" -> "OK",
        "InputCol" -> "Input",
        "InputCols" -> "col1 , col2 ,col3, col4",
        "OutputCol" -> "Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1",
        "Splits" -> "-0.5,0.0,0.5",
        "SplitsArray" -> "1.0 , 4.0 , 8.0"
      )
    ).build().get

    an [InvocationTargetException] must be thrownBy Bucketizer(
      Map(
        "HandleInvalid" -> "error",
        "InputCol" -> "Input",
        "InputCols" -> "col1 , col2 ,col3, col4",
        "OutputCol" -> "Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1",
        "Splits" -> "-0.5,0.0,0.5",
        "SplitsArray" -> "1.0 , 8.0"
      )
    ).build().get

    an [InvocationTargetException] must be thrownBy Bucketizer(
      Map(
        "HandleInvalid" -> "error",
        "InputCol" -> "Input",
        "InputCols" -> "col1 , col2 ,col3, col4",
        "OutputCol" -> "Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1",
        "Splits" -> "-0.5,0.5",
        "SplitsArray" -> "1.0 ,3.0, 8.0"
      )
    ).build().get
  }

}