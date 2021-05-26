package io.github.jsarni.CaraYaml

import java.io.FileNotFoundException

import io.github.jsarni.TestBase
import org.yaml.snakeyaml.scanner.ScannerException

class CaraYamlReaderTest extends TestBase {

    "loadFile" should "return parse the yaml description file to a json object" in {
      val caraPath = getClass.getResource("/cara.yaml").getPath
      val caraYaml = CaraYamlReader(caraPath)

      val result = caraYaml.loadFile()

      result.isSuccess shouldBe true
    }

    it should "Return an exception if the file does not exist" in {
      val caraPath = "/inexisting_model.yaml"
      val caraYaml = CaraYamlReader(caraPath)

      val result = caraYaml.loadFile()

      an [FileNotFoundException] should be thrownBy result.get
    }

    it should "Return an exception if the file format is not correct" in {
      val caraPath = getClass.getResource("/incorrect_cara.yaml").getPath
      val caraYaml = CaraYamlReader(caraPath)

      val result = caraYaml.loadFile()

      an [ScannerException] should be thrownBy result.get
    }


}
