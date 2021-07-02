package io.github.jsarni.caraml.carastage.datasetstage

import io.github.jsarni.TestBase
import org.apache.spark.ml.feature.{BucketedRandomProjectionLSH => fromSparkML}

class BucketedRandomProjectionLSHTest extends TestBase {

  "BucketedRandomProjectionLSH build Success" should
    "Build BucketedRandomProjectionLSH with the parametres given and be the same of Spark ML BucketedRandomProjectionLSH" in {

    val CaraDsFeature = new BucketedRandomProjectionLSH(
      Map(
        "BucketLength" -> "10.0",
        "InputCol" -> "Col_Input",
        "NumHashTables" -> "5",
        "OutputCol" -> "Col_Output",
        "Seed" -> "10"
      )
    )

    val SparkFeature=new fromSparkML()
      .setBucketLength(10)
      .setInputCol("Col_Input")
      .setNumHashTables(5)
      .setOutputCol("Col_Output")
      .setSeed(10)

    val CaraDsParams= CaraDsFeature.build().get.extractParamMap.toSeq.map(_.value).toList
    val SparkParams = SparkFeature.extractParamMap().toSeq.map(_.value).toList

    CaraDsParams should contain theSameElementsAs  SparkParams
  }

  "BucketedRandomProjectionLSHTest build Failure" should "Throw NumberFormatException " in {

    an [NumberFormatException] should be thrownBy BucketedRandomProjectionLSH(
      Map(
        "BucketLength" -> "10.0",
        "InputCol" -> "Col_Input",
        "NumHashTables" -> "wrong_value",
        "OutputCol" -> "Col_Output",
        "Seed" -> "10"
      )
    )
  }
}
