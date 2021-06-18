package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.classification.{DecisionTreeClassifier => SparkML}
import org.apache.spark.ml.PipelineStage
import scala.util.Try

case class DecisionTreeClassifier(
                                   CheckpointInterval: Option[Int], FeaturesCol: Option[String], Impurity: Option[String], LabelCol: Option[String], LeafCol: Option[String],
                                   MaxBins: Option[Int], MaxDepth: Option[Int], MinInfoGain: Option[Double],MinInstancesPerNode: Option[Int], MinWeightFractionPerNode: Option[Double],
                                   PredictionCol: Option[String], ProbabilityCol: Option[String], RawPredictionCol: Option[String], Seed: Option[Long], Thresholds: Option[Array[Double]],
                                   WeightCol: Option[String]
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
      params.get("ProbabilityCol"),
      params.get("RawPredictionCol"),
      params.get("Seed").map(_.toLong),
      params.get("Thresholds").map(_.split(",").map(_.toDouble)),
      params.get("WeightCol")
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
object DecisionTreeClassifier {
  def apply(params: Map[String, String]): DecisionTreeClassifier = new DecisionTreeClassifier(params)
}
