package io.github.jsarni.PipelineParser

import io.github.jsarni.CaraStage.Annotation.MapperConstructor

import scala.util.{Failure, Success}

trait ParserUtils {
  def getMapperConstructor(classToInspect: Class[_]) = {
    val mapperConstructoresList =
      classToInspect
        .getDeclaredConstructors()
        .toList
        .filter(_
          .getDeclaredAnnotations
          .filter(_.toString.startsWith(s"@${classOf[MapperConstructor].getName}")).nonEmpty
        )
    mapperConstructoresList.length match {
      case 1 => Success(mapperConstructoresList.head)
      case 0 => Failure(new NoSuchMethodException(s"No MapperConstructor Found for Class ${classToInspect.getName}"))
      case _ => Failure(new Exception(s"More than one MapperConstructor Found for Class ${classToInspect.getName}"))
    }
  }

}
