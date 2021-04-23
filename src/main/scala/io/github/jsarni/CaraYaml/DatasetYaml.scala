package io.github.jsarni.CaraYaml

final class DatasetYaml(yamlPath: String) extends CaraYaml {
  override val fileType: String = FileTypes.DATASET_FILE
  override val filePath: String = yamlPath
}
