package io.github.jsarni.DatasetStage
import io.github.jsarni.TestBase
import io.github.jsarni.CaraStage.CaraStageDescription
import io.github.jsarni.CaraStage.DatasetStage._

class CaraDatasetTest extends TestBase {
  "Word2Vec" should("build new Word2Vec") in {
    val word_to_vec = Word2Vec(Map("InputCol"->"Input","MaxIter"->"10",
                                   "MaxSentenceLength"->"12", "NumPartitions"->"2",
                                   "OutputCol"->"feature","Seed"-> "12","VectorSize"->"10", "MinCount" ->"2","StepSize"->"12.0"))
    word_to_vec.build()
  }
  "Binarizer" should("build new binarizer")in {
    val binarizer=Binarizer(Map("InputCol"->"Input","InputCols"->"col1 , col2 ,col3, col4",
    "OutputCol" ->"Output",
    "Threshold" -> "10.0",
      "OutputCols" -> "Col10 , Col11 ,Col_12, Col_vector_1",
      "Thresholds" -> "10.0 , 12.0 , 13.0"))
    binarizer.build()
  }
  "RegexTokenizer" should("Build new RegexTokenizer ")in {
    val RegexTokenize=RegexTokenizer(Map("Gaps"-> "false","InputCol"-> "Col1","MinTokenLength"->"1",
    "OutputCol"->"Col2",
    "Pattern"->"*",
    "ToLowercase"->"true"))
    RegexTokenize.build()
  }
}
