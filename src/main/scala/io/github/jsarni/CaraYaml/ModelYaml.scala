package io.github.jsarni.CaraYaml

final class ModelYaml(yamlPath: String) extends CaraYaml {
  override val header: String = "/model"
  override val path: String = yamlPath
}
