package io.github.jsarni.CaraYaml

final class DatasetYaml(yamlPath: String) extends CaraYaml {
  override val header: String = "/dataset"
  override val path: String = yamlPath
}
