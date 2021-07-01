package io.github.jsarni.carastage.datasetstage

import org.apache.spark.ml.feature.{RegexTokenizer => fromSparkML}

/**
 * @param Gaps
 * @param InputCol
 * @param MinTokenLength
 * @param OutputCol
 * @param Pattern
 * @param ToLowercase
 */
case class RegexTokenizer(Gaps: Option[Boolean],
                          InputCol: Option[String],
                          MinTokenLength: Option[Int],
                          OutputCol: Option[String],
                          Pattern: Option[String],
                          ToLowercase : Option[Boolean])
  extends CaraDataset[fromSparkML] {

  def this(params: Map[String, String]) = {
    this(
      params.get("Gaps").map(_.toBoolean),
      params.get("InputCol"),
      params.get("MinTokenLength").map(_.toInt),
      params.get("OutputCol"),
      params.get("Pattern"),
      params.get("ToLowercase").map(_.toBoolean)
    )
  }

}

object RegexTokenizer {
  def apply(params: Map[String, String]): RegexTokenizer = new RegexTokenizer(params)
}
