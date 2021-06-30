package io.github.jsarni.PipelineParser

import io.github.jsarni.CaraStage.{CaraStage, CaraStageDescription}
import io.github.jsarni.CaraStage.ModelStage.LogisticRegression
import io.github.jsarni.CaraStage.TuningStage.TuningStageDescription
import io.github.jsarni.CaraYaml.CaraYamlReader
import io.github.jsarni.TestBase
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.classification.{LogisticRegression => SparkLR}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.evaluation.Evaluator

import scala.util.Try

class CaraParserTest extends TestBase {

  "extractTuner" should "return parse the yaml description file to a json object" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractStages = PrivateMethod[Try[List[CaraStageDescription]]]('extractStages)
    val result = caraParser.invokePrivate(extractStages(myJson.get))

    val expectedResult = Seq(
      CaraStageDescription("LogisticRegression", Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")),
      CaraStageDescription("FeatureSelection", Map("Param1" -> "S", "Param2" -> "0.5", "Param3" -> "false"))
    )

    result.isSuccess shouldBe true
    result.get should contain theSameElementsAs expectedResult
  }

  "parseSingleStageMap" should "parse a CaraStageDescription to a CaraStage " in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = CaraParser(CaraYamlReader(caraPath))

    val params = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val stageDesc = CaraStageDescription("LogisticRegression", params)
    val parseSingleStageMap = PrivateMethod[Try[CaraStage[_ <: PipelineStage]]]('parseSingleStageMap)

    val res = caraParser.invokePrivate(parseSingleStageMap(stageDesc))

    res.isSuccess shouldBe true
    res.get.isInstanceOf[LogisticRegression] shouldBe true
    res.get.asInstanceOf[LogisticRegression].MaxIter shouldBe params.get("MaxIter").map(_.toInt)
    res.get.asInstanceOf[LogisticRegression].RegParam shouldBe params.get("RegParam").map(_.toDouble)
    res.get.asInstanceOf[LogisticRegression].ElasticNetParam shouldBe params.get("ElasticNetParam").map(_.toDouble)
  }

  "parseStages" should "parse a list of CaraStageDescription to the corresponding list of CaraStage" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = CaraParser(CaraYamlReader(caraPath))

    val params1 = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val params2 = Map("MaxIter" -> "20", "FitIntercept" -> "False", "ProbabilityCol" -> "col1")
    val stagesDesc = List(
      CaraStageDescription("LogisticRegression", params1),
      CaraStageDescription("LogisticRegression", params2)
    )

    val expectedResult = List(LogisticRegression(params1), LogisticRegression(params2))

    val parseStages = PrivateMethod[Try[List[CaraStage[_ <: PipelineStage]]]]('parseStages)
    val res = caraParser.invokePrivate(parseStages(stagesDesc))

    res.isSuccess shouldBe true
    res.get should contain theSameElementsAs expectedResult
  }

  "buildStages" should "build a list PipelineStages out of a list of CaraStages" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = CaraParser(CaraYamlReader(caraPath))

    val params1 = Map("MaxIter" -> "10", "RegParam" -> "0.3", "ElasticNetParam" -> "0.1")
    val params2 = Map("MaxIter" -> "20", "FitIntercept" -> "False", "ProbabilityCol" -> "col1")
    val stagesList = List(
      LogisticRegression(params1), LogisticRegression(params2)
    )

    val expectedResult = List(
      new SparkLR().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.1),
      new SparkLR().setMaxIter(20).setFitIntercept(false).setProbabilityCol("col1")
    )

    val buildStages = PrivateMethod[Try[List[PipelineStage]]]('buildStages)
    val res = caraParser.invokePrivate(buildStages(stagesList))

    res.isSuccess shouldBe true

    val resParameters = res.get.map(_.extractParamMap().toSeq.map(_.value))
    val expectedParameters = expectedResult.map(_.extractParamMap().toSeq.map(_.value))

    resParameters.head should contain theSameElementsAs expectedParameters.head
    resParameters(1) should contain theSameElementsAs expectedParameters(1)
  }

  "buildPipeline" should "build a Spark ML Pipeline out of a list of PipelineStages" in {
    val caraPath = getClass.getResource("/cara.yaml").getPath
    val caraParser = CaraParser(CaraYamlReader(caraPath))

    val stagesList = List(
      new SparkLR().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.1)
    )

    val buildPipeline = PrivateMethod[Try[Pipeline]]('buildPipeline)
    val res = caraParser.invokePrivate(buildPipeline(stagesList))

    res.isSuccess shouldBe true
    res.get.getStages shouldBe new Pipeline().setStages(stagesList.toArray).getStages
  }

  "parsePipeline" should "build the described Pipeline of the Yaml File" in {
    val caraPath = getClass.getResource("/cara_for_build.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)


    val parsePipeline = PrivateMethod[Try[Pipeline]]('parsePipeline)
    val res = caraParser.invokePrivate(parsePipeline())
    val exprectedRes = new Pipeline().setStages(Array(new SparkLR().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.1)))

    res.isSuccess shouldBe true
    res.get.getStages.map(_.extractParamMap().toSeq.map(_.value)).head should contain theSameElementsAs
      exprectedRes.getStages.map(_.extractParamMap().toSeq.map(_.value)).head
  }

  "extractTuner" should "get the correct Evaluator Name from the Yaml File" in {
    val caraPath = getClass.getResource("/cara_for_build.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractEvaluator = PrivateMethod[Try[String]]('extractEvaluator)
    val result = caraParser.invokePrivate(extractEvaluator(myJson.get))

    result.isSuccess shouldBe true
    result.get shouldBe "RegressionEvaluator"
  }

  it should "Raise an exception if there is no evaluator specified" in {
    val caraPath = getClass.getResource("/cara_zero_evaluator.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractEvaluator = PrivateMethod[Try[String]]('extractEvaluator)
    val result = caraParser.invokePrivate(extractEvaluator(myJson.get))

    result.isFailure shouldBe true
  }

  it should "Raise an exception if there is more than one evaluator specified" in {
    val caraPath = getClass.getResource("/cara_two_evaluator.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractEvaluator = PrivateMethod[Try[String]]('extractEvaluator)
    val result = caraParser.invokePrivate(extractEvaluator(myJson.get))

    result.isFailure shouldBe true
  }

  "parseEvaluator" should "build the described evaluator of the Yaml File" in {
    val caraPath = getClass.getResource("/cara_for_build.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val parseEvaluator = PrivateMethod[Try[Evaluator]]('parseEvaluator)
    val res = caraParser.invokePrivate(parseEvaluator())

    res.isSuccess shouldBe true
    res.get.isInstanceOf[RegressionEvaluator] shouldBe true
  }

  "extractTuner" should "get the correct Tuner Description from the Yaml File" in {
    val caraPath = getClass.getResource("/cara_for_build.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractTuner = PrivateMethod[Try[TuningStageDescription]]('extractTuner)
    val result = caraParser.invokePrivate(extractTuner(myJson.get))

    result.isSuccess shouldBe true
    result.get shouldBe TuningStageDescription("CrossValidator", "NumFolds", "3")
  }

  it should "raise an exception ilf there is more than one tuner in the Yaml File" in {
    val caraPath = getClass.getResource("/cara_two_evaluator.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractTuner = PrivateMethod[Try[TuningStageDescription]]('extractTuner)
    val result = caraParser.invokePrivate(extractTuner(myJson.get))

    result.isFailure shouldBe true
    an [IllegalArgumentException] should be thrownBy result.get
  }

  "parseTuner" should "build the described Tuner of the Yaml File" in {
    val caraPath = getClass.getResource("/cara_for_build.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val myJson = caraYaml.loadFile()

    val extractTuner = PrivateMethod[Try[TuningStageDescription]]('extractTuner)
    val result = caraParser.invokePrivate(extractTuner(myJson.get))

    result.isSuccess shouldBe true
    result.get shouldBe TuningStageDescription("CrossValidator", "NumFolds", "3")
  }

  "build" should "build the described Pipeline of the Yaml File" in {
    val caraPath = getClass.getResource("/cara_for_build.yaml").getPath
    val caraYaml = CaraYamlReader(caraPath)
    val caraParser = CaraParser(caraYaml)

    val res = caraParser.build()

    val exprectedRes = new Pipeline().setStages(Array(new SparkLR().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.1)))

    res.isSuccess shouldBe true
    res.get.evaluator.isInstanceOf[RegressionEvaluator] shouldBe true
    res.get.pipeline.getStages.map(_.extractParamMap().toSeq.map(_.value)).head should contain theSameElementsAs
      exprectedRes.getStages.map(_.extractParamMap().toSeq.map(_.value)).head
    res.get.tuner shouldBe Some(TuningStageDescription("CrossValidator", "NumFolds", "3"))
  }
}
