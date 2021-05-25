package io.github.jsarni.DatasetStage
import io.github.jsarni.TestBase
import io.github.jsarni.CaraStage.CaraStageDescription
import io.github.jsarni.CaraStage.DatasetStage._

class CaraDatasetTest extends TestBase {
  "Word2Vec" should("build new Word2Vec") in {
    val wordToVec = Word2Vec(Map("InputCol"->"Input","MaxIter"->"10",
                                   "MaxSentenceLength"->"12", "NumPartitions"->"2",
                                   "OutputCol"->"feature","Seed"-> "12","VectorSize"->"10", "MinCount" ->"2","StepSize"->"12.0"))
    wordToVec.build()
  }

  "BucketedRandomProjectionLSH" should("Build new BucketedRandomProjectionLSH")in {
    val BucketRPLSH= BucketedRandomProjectionLSH(
      Map("BucketLength"-> "10.0",
          "InputCol"-> "Col1",
          "NumHashTables"-> "1",
          "OutputCol"-> "ColFinal",
          "Seed"-> "12"))
    BucketRPLSH.build()
  }
  "BucketedRandomProjectionLSHModel" should("Build new BucketedRandomProjectionLSHModel")in {
    val BucketRPLSHModel= BucketedRandomProjectionLSHModel(
      Map("BucketLength"-> "10.0",
        "InputCol"-> "Col1",
        "NumHashTables"-> "1",
        "OutputCol"-> "ColFinal",
        "Seed"-> "12"))
    BucketRPLSHModel.build()
  }
  "Bucketizer" should("build new Bucketizer")in {
    val Bucketiz=Bucketizer(
      Map(
        "OutputCol" ->"Output",
        "HandleInvalid"-> "a",
        "InputCols"-> "Col1,Col2,Col3",
        "OutputCols"->"",
        "Splits"->"10.0,12.0",
        "SplitsArray"->"10.0, 12.0 ",
        "InputCol"->"Input"))
    Bucketiz.build()
  }
  "RegexTokenizer" should("Build new RegexTokenizer ")in {
    val RegexTokenize=RegexTokenizer(Map("Gaps"-> "false","InputCol"-> "Col1","MinTokenLength"->"1",
    "OutputCol"->"Col2",
    "Pattern"->"*",
    "ToLowercase"->"true"))
    RegexTokenize.build()
  }
  "CountVectorizerModel" should ("Build new CountVectorizerModel")in {
    val CountVModel = CountVectorizerModel(Map("Binary"->"true",
      "InputCol"->"Col1",
      "MaxDF"->"10.0",
      "MinDF"->"2.0",
      "MinTF"->"1.0",
      "OutputCol"->"ColFinal",
      "Vocabulary"-> "a,b,c,d"))
    CountVModel.build()
  }
  "CountVectorizer" should ("Build new CountVectorizer")in {
    val CountV = CountVectorizer(Map("Binary"->"true",
      "InputCol"->"Col1",
      "MaxDF"->"10.0",
      "MinDF"->"2.0",
      "MinTF"->"1.0",
      "OutputCol"->"ColFinal",
      "VocabSize"->"5"))
    CountV.build()
  }
}
