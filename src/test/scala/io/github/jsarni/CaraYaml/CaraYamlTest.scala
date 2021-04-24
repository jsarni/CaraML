package io.github.jsarni.CaraYaml

import io.github.jsarni.TestBase

class CaraYamlTest extends TestBase {

    "loadFile" should "return parse the yaml description file to a json object" in {
      val caraPath = getClass.getResource("/cara.yaml").getPath
      val caraYaml = CaraYaml(caraPath)

      val result = caraYaml.loadFile()

      result.isSuccess shouldBe true
    }

    it should "Return an exception if the file does not exist" in {
      val caraPath = "/inexisting_model.yaml"
      val caraYaml = CaraYaml(caraPath)

      val result = caraYaml.loadFile()

      // TODO: Test on exception type
      //    modelResult.get shouldBe isInstanceOf[FileNotFoundException]
      result.isFailure shouldBe true
    }

    it should "Return an exception if the file format is not correct" in {
      val caraPath = getClass.getResource("/incorrect_cara.yaml").getPath
      val caraYaml = CaraYaml(caraPath)

      val result = caraYaml.loadFile()

      // TODO: Test on exception type
      //    modelResult.get shouldBe isInstanceOf[FileNotFoundException]
      result.isFailure shouldBe true
    }


}
