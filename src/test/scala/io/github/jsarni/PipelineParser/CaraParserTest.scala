package io.github.jsarni.PipelineParser

import io.github.jsarni.TestBase
import io.github.jsarni.PipelineParser.CaraParser
import io.github.jsarni.PipelineParser.DatasetStage.Word2Vec
import io.github.jsarni.PipelineParser.ModelStage.LogisticRegressionStage

import scala.io.Source

class CaraParserTest extends TestBase {

  "getFileType" should "return correct file type" in {
    val modelPath = getClass.getResource("/model.yaml").getPath
    val modelParser = new CaraParser[LogisticRegressionStage](modelPath)

    val datasetPath = getClass.getResource("/dataset.yaml").getPath
    val datasetParser = new CaraParser[Word2Vec](datasetPath)

    modelParser.getFileType[LogisticRegressionStage]() shouldBe "/model"
    datasetParser.getFileType[Word2Vec]() shouldBe "/dataset"
  }

}
