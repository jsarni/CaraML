package io.github.jsarni.PipelineParser.DatasetStage
import org.apache.spark.ml.feature.{Word2Vec=> WordtoVec}
import org.apache.spark.ml.linalg.Vector
case class Word2Vec(InputCol: String,OutputCol:String, VectorSize:Int,MinCount:Int) extends DatasetStage {
    val wordtovec=new WordtoVec()
                      .setInputCol(InputCol)
                      .setOutputCol(OutputCol)
                      .setVectorSize(VectorSize)
                      .setMinCount(MinCount)
}

