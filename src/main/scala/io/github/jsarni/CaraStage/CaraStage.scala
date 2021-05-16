package io.github.jsarni.CaraStage

import org.apache.spark.ml.PipelineStage
import java.lang.reflect.Method
import scala.util.Try

trait CaraStage {

  def build(): Try[PipelineStage]

  // Function to get methode by name and do invoke with the right params types and values
  def getMethode(stage : PipelineStage, field : Any, fieldName : String): Method = {
    val methodeName = "set" + fieldName
    field match {
      case _ : Any  if field.getClass == Array[Array[Boolean]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Boolean]]].getClass )
      case _ : Any  if field.getClass == Array[Array[Double]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Double]]].getClass )
      case _ : Any  if field.getClass == Array[Array[String]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[String]]].getClass )
      case _ : Any  if field.getClass == Array[Array[Float]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Float]]].getClass )
      case _ : Any  if field.getClass == Array[Array[Short]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Short]]].getClass )
      case _ : Any  if field.getClass == Array[Array[Char]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Char]]].getClass )
      case _ : Any  if field.getClass == Array[Array[Byte]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Byte]]].getClass )
      case _ : Any  if field.getClass == Array[Array[Long]]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Array[Long]]].getClass )
      case _ : Any  if field.getClass == Array[Boolean]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Boolean]].getClass )
      case _ : Any  if field.getClass == Array[Double]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Double]].getClass )
      case _ : Any  if field.getClass == Array[String]().getClass  =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[String]].getClass )
      case _ : Any  if field.getClass == Array[Float]().getClass   =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Float]].getClass )
      case _ : Any  if field.getClass == Array[Short]().getClass   =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Short]].getClass )
      case _ : Any  if field.getClass == Array[Char]().getClass    =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Char]].getClass )
      case _ : Any  if field.getClass == Array[Byte]().getClass    =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Byte]].getClass )
      case _ : Any  if field.getClass == Array[Long]().getClass    =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Long]].getClass )
      case _ : Any  if field.getClass == Array[Int]().getClass     =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Array[Int]].getClass )
      case _ : java.lang.Boolean   =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Boolean].getClass )
      case _ : java.lang.Double    =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Double].getClass )
      case _ : java.lang.Float     =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Float].getClass )
      case _ : java.lang.Short     =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Short].getClass )
      case _ : java.lang.Character =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Char].getClass )
      case _ : java.lang.Byte      =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Byte].getClass )
      case _ : java.lang.Long      =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Long].getClass)
      case _ : java.lang.Integer   =>   stage.getClass.getMethod(methodeName, field.asInstanceOf[Int].getClass)
      case _ : java.lang.String    =>   stage.getClass.getMethod(methodeName, field.getClass )
    }
  }

}
