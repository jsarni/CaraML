package io.github.jsarni.CaraStage.ModelStage

import io.github.jsarni.CaraStage.Annotation.MapperConstructor
import org.apache.spark.ml.PipelineStage
import org.apache.spark.ml.classification.{LogisticRegression => log}



case class LogisticRegression(MaxIter: Option[Int], RegParam: Option[Double], ElasticNetParam: Option[Double], Family:Option[String])
  extends CaraModel {

  @MapperConstructor
  def this(params: Map[String, String]) = {
    this(
      params.get("MaxIter").map(_.toInt),
      params.get("RegParam").map(_.toDouble),
      params.get("ElasticNetParam").map(_.toDouble),
      params.get("Family").map(_.toString)
    )
  }
  def GetMethode(lr : PipelineStage, field : Any, field_name : String)  = {
    val MethodeName = "set"+field_name
    field match {
      case _ :java.lang.Long => lr.getClass.getMethod(MethodeName, field.asInstanceOf[Long].getClass)
      case _: java.lang.Integer =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Int].getClass)
      case _ : java.lang.Double =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Double].getClass )
      case _ : java.lang.Float  =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Float].getClass )
      case _ : java.lang.Character =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Char].getClass )
      case _ : java.lang.Byte =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Byte].getClass )
      case _ : java.lang.Boolean =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Boolean].getClass )
      case _ : java.lang.Short =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Short].getClass )
      case _ : java.lang.String =>   lr.getClass.getMethod(MethodeName, field.getClass )


    }
  }
  override def build(): PipelineStage = {
    val lr = new log()
    val definedFields = this.getClass.getDeclaredFields.filter(f => f.get(this).asInstanceOf[Option[Any]].isDefined)
    val names = definedFields.map(f => f.getName)
    val values = definedFields.map(f => f.get(this))
    val zipFields = names zip values
    zipFields.map(f=>  GetMethode(lr,f._2 match {case Some(s) => s },f._1).invoke(lr,(f._2 match {case Some(value) => value.asInstanceOf[f._2.type ] })))
    lr

  }
}
object LogisticRegression {
  def apply(params: Map[String, String]): LogisticRegression = new LogisticRegression(params)
}





