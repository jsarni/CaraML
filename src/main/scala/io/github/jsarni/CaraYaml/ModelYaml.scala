package io.github.jsarni.CaraYaml

final class ModelYaml(yamlPath: String) extends CaraYaml {
  override val fileType: String = FileTypes.MODEL_FILE
  override val filePath: String = yamlPath
}
