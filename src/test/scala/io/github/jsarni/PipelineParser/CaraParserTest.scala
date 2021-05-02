package io.github.jsarni.PipelineParser

import io.github.jsarni.CaraStage.CaraStageDescription
import io.github.jsarni.CaraStage.ModelStage.LogisticRegression
import io.github.jsarni.CaraYaml.CaraYaml
import io.github.jsarni.TestBase

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

  "parseStageMap" should "return parse the yaml description file to a json object" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = new CaraParser(CaraYaml(caraPath))

    val params = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val stageDesc =
      CaraStageDescription("LogisticRegression", params)

    val res = caraParser.parseStageMap(stageDesc)


    res.isInstanceOf[LogisticRegression] shouldBe true
    res.asInstanceOf[LogisticRegression].MaxIter shouldBe params.get("MaxIter").map(_.toInt)
    res.asInstanceOf[LogisticRegression].RegParam shouldBe params.get("RegParam").map(_.toDouble)
    res.asInstanceOf[LogisticRegression].ElasticNetParam shouldBe params.get("ElasticNetParam").map(_.toDouble)

  }
}
