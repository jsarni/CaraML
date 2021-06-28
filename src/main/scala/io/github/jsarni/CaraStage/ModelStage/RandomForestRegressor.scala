package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.regression.{RandomForestRegressor => SparkML}
import org.apache.spark.ml.PipelineStage
import scala.util.Try

case class RandomForestRegressor(
                                  CheckpointInterval: Option[Int], FeaturesCol: Option[String], Impurity: Option[String], LabelCol: Option[String], LeafCol: Option[String],
                                  MaxBins: Option[Int], MaxDepth: Option[Int], MinInfoGain: Option[Double],MinInstancesPerNode: Option[Int], MinWeightFractionPerNode: Option[Double],
                                  PredictionCol: Option[String], Seed: Option[Long], WeightCol: Option[String], FeatureSubsetStrategy: Option[String], SubsamplingRate: Option[Double],
                                  NumTrees: Option[Int]
                                )
  extends CaraModel {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("CheckpointInterval").map(_.toInt),
      params.get("FeaturesCol"),
      params.get("Impurity"),
      params.get("LabelCol"),
      params.get("LeafCol"),
      params.get("MaxBins").map(_.toInt),
      params.get("MaxDepth").map(_.toInt),
      params.get("MinInfoGain").map(_.toDouble),
      params.get("MinInstancesPerNode").map(_.toInt),
      params.get("MinWeightFractionPerNode").map(_.toDouble),
      params.get("PredictionCol"),
      params.get("Seed").map(_.toLong),
      params.get("WeightCol"),
      params.get("FeatureSubsetStrategy"),
      params.get("SubsamplingRate").map(_.toDouble),
      params.get("NumTrees").map(_.toInt)
    )
  }

  override def build(): Try[PipelineStage] = Try {
    val model = new SparkML()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map { f =>
      val fieldName = f._1
      val fieldValue = f._2 match {case Some(s) => s }
      getMethode(model,fieldValue,fieldName)
        .invoke(model,fieldValue.asInstanceOf[f._2.type])
    }
    model
  }
}
object RandomForestRegressor {
  def apply(params: Map[String, String]): RandomForestRegressor = new RandomForestRegressor(params)
}

