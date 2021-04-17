package io.github.jsarni.PipelineParser

import io.github.jsarni.CaraStage.CaraStage
import io.github.jsarni.CaraStage.DatasetStage.Word2Vec
import io.github.jsarni.CaraStage.ModelStage.LogisticRegressionStage
import io.github.jsarni.CaraYaml.{DatasetYaml, ModelYaml}
import io.github.jsarni.TestBase
import io.github.jsarni.PipelineParser.CaraParser
import org.codehaus.jackson.JsonNode

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


    val extractStages = PrivateMethod[List[CaraStage]]('extractStages)
    val result = modelParser.invokePrivate(extractStages(myJson.get))

    val expectedResult =
      Seq(
        CaraStage("LogisticRegression", Some(Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")))
      )
    result should contain theSameElementsAs expectedResult
  }

}
