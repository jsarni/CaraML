package io.github.jsarni.PipelineParser

import io.github.jsarni.TestBase
import io.github.jsarni.CaraStage.CaraStageDescription
import io.github.jsarni.CaraStage.ModelStage.{TestStage, LogisticRegression}
import io.github.jsarni.CaraYaml.{DatasetYaml, ModelYaml}
import org.codehaus.jackson.JsonNode
import io.github.jsarni.PipelineParser.CaraParser

import java.io.FileNotFoundException
import scala.io.Source
import scala.util.Try

class CaraParserTest extends TestBase {
  "loadFile" should "return parse the yaml description file to a json object" in {
    val modelPath = getClass.getResource("/model.yaml").getPath
    val modelYaml = new ModelYaml(modelPath)
    val modelParser = new CaraParser(modelYaml)

    val datasetPath = getClass.getResource("/dataset.yaml").getPath
    val datasetYaml = new DatasetYaml(datasetPath)
    val datasetParser = new CaraParser(datasetYaml)

    val loadFile = PrivateMethod[Try[JsonNode]]('loadFile)
    val modelResult = modelParser.invokePrivate(loadFile())
    val datasetResult = datasetParser.invokePrivate(loadFile())

    modelResult.isSuccess shouldBe true
    datasetResult.isSuccess shouldBe true
  }

  it should "Return an exception if the file does not exist" in {
    val modelPath = "/inexisting_model.yaml"
    val modelYaml = new ModelYaml(modelPath)
    val modelParser = new CaraParser(modelYaml)


    val loadFile = PrivateMethod[Try[JsonNode]]('loadFile)
    val modelResult = modelParser.invokePrivate(loadFile())

    // TODO: Test on exception type
    //    modelResult.get shouldBe isInstanceOf[FileNotFoundException]
    modelResult.isFailure shouldBe true
  }

  it should "Return an exception if the file format is not correct" in {
    val modelPath = getClass.getResource("/incorrect_model.yaml").getPath
    val modelYaml = new ModelYaml(modelPath)
    val modelParser = new CaraParser(modelYaml)


    val loadFile = PrivateMethod[Try[JsonNode]]('loadFile)
    val modelResult = modelParser.invokePrivate(loadFile())

    // TODO: Test on exception type
    //    modelResult.get shouldBe isInstanceOf[FileNotFoundException]
    modelResult.isFailure shouldBe true
  }

  "extractStages" should "return parse the yaml description file to a json object" in {
    val modelPath = getClass.getResource("/model.yaml").getPath
    val modelYaml = new ModelYaml(modelPath)
    val modelParser = new CaraParser(modelYaml)

    val loadFile = PrivateMethod[Try[JsonNode]]('loadFile)
    val myJson = modelParser.invokePrivate(loadFile())


    val extractStages = PrivateMethod[List[CaraStageDescription]]('extractStages)
    val result = modelParser.invokePrivate(extractStages(myJson.get))

    val expectedResult =
      Seq(
        CaraStageDescription("LogisticRegression", Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1"))
      )
    result should contain theSameElementsAs expectedResult
  }


  "parseStage" should "return parse the yaml description file to a json object" in {
    val modelPath = getClass.getResource("/model.yaml").getPath
    val modelYaml = new ModelYaml(modelPath)
    val modelParser = new CaraParser(modelYaml)

    val stageDesc =
      CaraStageDescription("TestStage", Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1"))

    val res = modelParser.parseStage(stageDesc)
    print(res.get)

  }

  "parseStageMap" should "return parse the yaml description file to a json object" in {
    val modelPath = getClass.getResource("/model.yaml").getPath
    val modelYaml = new ModelYaml(modelPath)
    val modelParser = new CaraParser(modelYaml)

    val params = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val stageDesc =
      CaraStageDescription("TestStage", params)

    val res = modelParser.parseStageMap(stageDesc)


    res.isInstanceOf[TestStage] shouldBe true
    res.asInstanceOf[TestStage].MaxIter shouldBe params.get("MaxIter").map(_.toInt)
    res.asInstanceOf[TestStage].RegParam shouldBe params.get("RegParam").map(_.toDouble)
    res.asInstanceOf[TestStage].ElasticNetParam shouldBe params.get("ElasticNetParam").map(_.toDouble)

  }
  "LogisticRegression" should("Create an lr model and set all parameters with there args values") in {
    val basicParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial")
    val allParams = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1", "Family" -> "multinomial" ,"FeaturesCol" -> "FeatureColname"
      , "FitIntercept" -> "True", "PredictionCol" -> "Age", "ProbabilityCol" -> "ProbaColname", "RawPredictionCol"-> "RawPredictColname"
      , "Standardization" -> "True" , "Tol" -> "0.13", "WeightCol" -> "WeightColname")

    val lr = LogisticRegression( basicParams)
    val lr1 = LogisticRegression( allParams)
    lr.build()
    lr1.build()

  }
}
