package io.github.jsarni.CaraStage.DatasetStage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{Bucketizer => fromSparkML}
import java.lang.reflect.InvocationTargetException
import java.lang.IllegalArgumentException
class BucketizerTest extends TestBase {
  "Bucketizer Build Success" should("build new Bucketizer with parametres given on the Map and be the same with SparkMl Bucketizer")in {
    val CaraDsFeature=Bucketizer(
      Map ("HandleInvalid"->"skip",
        "InputCol"->"Input",
        "InputCols"->"col1 , col2 ,col3, col4",
        "OutputCol" ->"Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1"
        ,"Splits" -> "-0.5,0.0,0.5"
        ,"SplitsArray" -> "1.0 , 4.0 , 8.0"
      ))
    val SparkFeature=new fromSparkML()
      .setHandleInvalid("skip") //error or keep
      .setInputCol("Input")
      .setInputCols(Array("col1" , "col2" ,"col3", "col4"))
      .setOutputCol("Output")
      .setOutputCols(Array("Col10" , "Col11" ,"Col_12", "Col_vector_1"))
      .setSplits(Array(-0.5,0.0,0.5))
      .setSplitsArray(Array(Array(1.0 , 4.0 , 8.0)))

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
//    print(CaraDsFeature.build().get.asInstanceOf[fromSparkML].getSplitsArray)
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList
    CaraDsParams should contain theSameElementsAs  SparkParams

  }
  "Bucketizer build failure" should "fail to build new Bucketizer when wrong parameter is set" in {
    println("HandlInvalid parameter must be one of : error, skip, keep")
    an [InvocationTargetException] must be thrownBy Bucketizer(
      Map ("HandleInvalid"->"OK",
        "InputCol"->"Input",
        "InputCols"->"col1 , col2 ,col3, col4",
        "OutputCol" ->"Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1"
        ,"Splits" -> "-0.5,0.0,0.5"
        ,"SplitsArray" -> "1.0 , 4.0 , 8.0"
      )).build().get

    println("SplitsArray parameter must be est correctly")
    an [InvocationTargetException] must be thrownBy Bucketizer(
      Map ("HandleInvalid"->"error",
        "InputCol"->"Input",
        "InputCols"->"col1 , col2 ,col3, col4",
        "OutputCol" ->"Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1"
        ,"Splits" -> "-0.5,0.0,0.5"
        ,"SplitsArray" -> "1.0 , 8.0"
      )).build().get
    println("Splits parameter must be est correctly")
    an [InvocationTargetException] must be thrownBy Bucketizer(
      Map ("HandleInvalid"->"error",
        "InputCol"->"Input",
        "InputCols"->"col1 , col2 ,col3, col4",
        "OutputCol" ->"Output",
        "OutputCols" -> "Col10 , Col11,Col_12,Col_vector_1"
        ,"Splits" -> "-0.5,0.5"
        ,"SplitsArray" -> "1.0 ,3.0, 8.0"
      )).build().get
  }

}
