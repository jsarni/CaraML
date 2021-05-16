package io.github.jsarni.DatasetLoader

import org.apache.spark.sql.{DataFrame, SparkSession}
import scala.util.Try

class CaraLoader(path: String, format: String)(implicit spark: SparkSession) {

  def load(): Try[DataFrame] = {
    Try{
      spark.read.format(format).load(path)
    }
  }
}

object CaraLoader {
  def apply(path: String, format: String)(implicit spark: SparkSession): CaraLoader = new CaraLoader(path, format)(spark)
}