package io.github.jsarni.PipelineParser

import io.github.jsarni.CaraStage.{CaraStage, CaraStageDescription}
import io.github.jsarni.CaraStage.ModelStage.LogisticRegression
import io.github.jsarni.CaraYaml.CaraYaml
import io.github.jsarni.TestBase
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.classification.{LogisticRegression => SparkLR}

class CaraParserTest extends TestBase {

  "extractStages" should "return parse the yaml description file to a json object" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraYaml = CaraYaml(caraPath)
    val caraParser = new CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractStages = PrivateMethod[List[CaraStageDescription]]('extractStages)
    val result = caraParser.invokePrivate(extractStages(myJson.get))

    val expectedResult =
      Seq(
        CaraStageDescription("LogisticRegression", Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")),
        CaraStageDescription("FeatureSelection", Map("Param1" -> "S", "Param2" -> "0.5", "Param3" -> "false"))
      )

    result should contain theSameElementsAs expectedResult
  }


//  "parseStage" should "return parse the yaml description file to a json object" in {
//    val caraPath = getClass.getResource("/cara.yaml").getPath
//    val caraYaml = CaraYaml(caraPath)
//    val caraParser = new CaraParser(caraYaml)
//
//
//    val stageDesc =
//      CaraStageDescription("TestStage", Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1"))
//
//    val res = caraParser.parseStage(stageDesc)
//    print(res.get)
//
//  }

  "parseStageMap" should "parse a CaraStageDescription to a CaraStage " in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = new CaraParser(CaraYaml(caraPath))

    val params = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val stageDesc =
      CaraStageDescription("LogisticRegression", params)

    val parseStageMap = PrivateMethod[CaraStage]('parseStageMap)

    val res = caraParser.invokePrivate(parseStageMap(stageDesc))

    res.isInstanceOf[LogisticRegression] shouldBe true
    res.asInstanceOf[LogisticRegression].MaxIter shouldBe params.get("MaxIter").map(_.toInt)
    res.asInstanceOf[LogisticRegression].RegParam shouldBe params.get("RegParam").map(_.toDouble)
    res.asInstanceOf[LogisticRegression].ElasticNetParam shouldBe params.get("ElasticNetParam").map(_.toDouble)
  }

  "parseAllStages" should "parse a list of CaraStageDescription to the corresponding list of CaraStage" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = new CaraParser(CaraYaml(caraPath))

    val params1 = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val params2 = Map("MaxIter" -> "20", "FitIntercept" -> "False", "ProbabilityCol" -> "col1")
    val stagesDesc = List(
      CaraStageDescription("LogisticRegression", params1),
      CaraStageDescription("LogisticRegression", params2)
    )

    val expectedResult = List(LogisticRegression(params1), LogisticRegression(params2))

    val parseAllStages = PrivateMethod[List[CaraStage]]('parseAllStages)
    val res = caraParser.invokePrivate(parseAllStages(stagesDesc))

    res should contain theSameElementsAs expectedResult
  }

  "buildAllStages" should "build a list PipelineStages out of a list of CaraStages" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = new CaraParser(CaraYaml(caraPath))

    val params1 = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val params2 = Map("MaxIter" -> "20", "FitIntercept" -> "False", "ProbabilityCol" -> "col1")
    val stagesList = List(
      LogisticRegression(params1), LogisticRegression(params2)
    )

    val expectedResult = List(
      new SparkLR().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.1),
      new SparkLR().setMaxIter(20).setFitIntercept(false).setProbabilityCol("col1")
    )

    val buildAllStages = PrivateMethod[List[PipelineStage]]('buildAllStages)
    val res = caraParser.invokePrivate(buildAllStages(stagesList))

    val resParameters = res.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head
    resParameters(1) should contain theSameElementsAs expectedParameters(1)
  }

  "buildPipeline" should "build a Spark ML Pipeline out of a list of PipelineStages" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = new CaraParser(CaraYaml(caraPath))

    val stagesList = List(
      new SparkLR().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.1)
    )

    val buildPipeline = PrivateMethod[Pipeline]('buildPipeline)
    val res = caraParser.invokePrivate(buildPipeline(stagesList))

    res.getStages shouldBe new Pipeline().setStages(stagesList.toArray).getStages
  }
}
