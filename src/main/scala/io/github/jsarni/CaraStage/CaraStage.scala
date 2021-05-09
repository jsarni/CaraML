package io.github.jsarni.CaraStage

import org.apache.spark.ml.PipelineStage
import scala.util.Try

trait CaraStage {

  //TODO: Add builder function
  def build(): Try[PipelineStage]

  // Function to get methode by name and do invoke with the right params types and values
  def GetMethode(lr : PipelineStage, field : Any, field_name : String)  = {
    val MethodeName = "set"+field_name
    field match {
      case _ : Any  if field.getClass == Array[Double]().getClass  =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Double]].getClass )
      case _ : Any  if field.getClass == Array[String]().getClass  =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[String]].getClass )
      case _ : Any  if field.getClass == Array[Float]().getClass   =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Float]].getClass )
      case _ : Any  if field.getClass == Array[Short]().getClass   =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Short]].getClass )
      case _ : Any  if field.getClass == Array[Char]().getClass    =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Char]].getClass )
      case _ : Any  if field.getClass == Array[Byte]().getClass    =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Byte]].getClass )
      case _ : Any  if field.getClass == Array[Long]().getClass    =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Long]].getClass )
      case _ : Any  if field.getClass == Array[Int]().getClass     =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Array[Int]].getClass )
      case _ : java.lang.Boolean   =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Boolean].getClass )
      case _ : java.lang.Double    =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Double].getClass )
      case _ : java.lang.Float     =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Float].getClass )
      case _ : java.lang.Short     =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Short].getClass )
      case _ : java.lang.Character =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Char].getClass )
      case _ : java.lang.Byte      =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Byte].getClass )
      case _ :java.lang.Long       =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Long].getClass)
      case _: java.lang.Integer    =>   lr.getClass.getMethod(MethodeName, field.asInstanceOf[Int].getClass)
      case _ : java.lang.String    =>   lr.getClass.getMethod(MethodeName, field.getClass )
    }
  }

}
