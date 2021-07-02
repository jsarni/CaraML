# CaraML

## *Presentation*

CaraMl is a Scala/Apache Spark framework for distributed Machine Learning programs, using the Apache Spark MLlib in the simplest possible way. No need to write hundreds or thousands code lines, just discribing pipline of models and/or transformations. The purpose is to do "Machine Learning as Code" 



## *Requirements*

To use CaraML framework, you must satisfy the following requirements:

- Scala version >= 2.12.13
- Spark version >= 3.1
- CaraML library


## *Installation* 

  - Spark :  [Download here](https://spark.apache.org/downloads.html) 
  - Scala : [Download here](https://www.scala-lang.org/download/)
  - CaraML library : [CaraML](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/jsarni/caraml_2.12/1.0.0-SNAPSHOT/)



## *Usage*

To use CaraML, you can add the framework dependency in your Spark application

- Sbt

```scala
  libraryDependencies += "io.github.jsarni" %% "caraml" % "1.0.0"
```

- Gradle

```scala
    compile group: 'io.github.jsarni"', name: 'caraml', version: '1.0.0'
```
- Maven

```scala
    <dependency>
    <groupId>io.github.jsarni"</groupId>
    <artifactId>caraml</artifactId>
    <version>1.0.0</version>
    </dependency>
```

CaraML needs the following information 

- Prepared dataset that will be used to transform and train models
- Path where to save the final trained model and its metrics
- Path of the CaraYaml file, where the user will declare and set the pipeline with stages of SparkML models and/or SparkML transformations

The Yaml file will be used to describe a pipeline of stages, each stage could be a SparkML model or a Spark ML method of data preprocessing.
All CaraYaml files must start with "CaraPipeline:" keyword and could contain the following keywords 

### *CaraPipeline*
* **"CaraPipeline:"** : keyword that must be set in the beginning of each CaraYaml file


### *Stage*
* **"- stage:"** Is a keyword used to declare and describe a stage. It could be an Estimator or a Transformer :
  * **SparkML Estimator** : Which is the name of the SparkML model that you want to use in the stage. 
  * **SparkML Transformer** : Is the name of SparkML feature transformation that you want to apply to your dataset (preprocessing)

    
Each stage will be followed by "params:" keyword, which contain one or many parameters/hyperparameters of the stage and their values.

```yaml
    params:
        - "Param1 name" : "Param value"
        - "Param2 name" : "Param value"
        - ....
        - "Paramn name" : "Param value"
```

### *Evaluator*
* **"- evaluator:"** Which is used to evaluate model output and  returns scalar metrics


### Tuner
* **"- tuner:"** Which is used for tuning ML algorithms that allow users to optimize hyperparameters in algorithms and Pipelines

Each tuner will be followed by "params:" keyword, which contain one or many parameters/hyperparameters of the tuner and their values.

```yaml
    params:
        - "Param1 name" : "Param value"
        - "Param2 name" : "Param value"
        - ....
        - "Paramn name" : "Param value"
```

### **CaraYaml example**
```yaml
CaraPipeline:
- stage: LogisticRegression
  params:
    - MaxIter: 5
    - RegParam: 0.3
    - ElasticNetParam: 0.8
- stage: Tokenizer
  params:
    - InputCol: Input
    - OutputCol: ResCol
    
    
- evaluator: MulticlassClassificationEvaluator
- tuner: TrainValidationSplit
  params:
    - TrainRatio: 0.8


```

**For more details and documentation you can refer to the Spark [MLlib](https://spark.apache.org/docs/3.1.2/ml-guide.html) documentation**



## *SparkML available components in CaraML*

This section lists all available SparkML components that you can use with CaraML framework

### *Models*

* **Classification**
  
  - LogisticRegression [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#logistic-regression) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/classification/LogisticRegression.html) 
  - DecisionTreeClassifier [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#decision-tree-classifier) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/classification/DecisionTreeClassifier.html)
  - GBTClassifier (Gradient-boosted tree classifier) [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#gradient-boosted-tree-classifier) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/classification/GBTClassifier.html)
  - NaiveBayes [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#naive-bayes) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/classification/NaiveBayes.html)
  - RandomForestClassifier [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#random-forest-classifier) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/classification/RandomForestClassifier.html)

* **Regression**

  - LinearRegression [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#linear-regression) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/regression/LinearRegression.html)
  - DecisionTreeRegressor [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#decision-tree-regressionhttps://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/regression/DecisionTreeRegressor.html) and [Documontation]()
  - RandomForestRegressor [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#random-forest-regression) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/regression/RandomForestRegressor.html)
  - GBTRegressor (Gradient-boosted tree Regressor) [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#gradient-boosted-tree-regression) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/regression/GBTRegressor.html)


* **Clustering**

  - K-means [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-clustering.html#k-means) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/clustering/KMeans.html) 
  - LDA (Latent Dirichlet allocation) [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-clustering.html#latent-dirichlet-allocation-lda) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/clustering/LDA.html)

### *Dataset operation*

- Binarizer [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#binarizer) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/Binarizer.html)
- BucketedRandomProjectionLSH [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#bucketed-random-projection-for-euclidean-distance) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/BucketedRandomProjectionLSH.html)
- Bucketizer [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#bucketizer) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/Bucketizer.html)
- ChiSqSelector [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#chisqselector) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/ChiSqSelector.html)
- CountVectorizer [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#countvectorizer) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/CountVectorizer.html)
- HashingTF [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#tf-idf) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/HashingTF.html)
- IDF [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#tf-idf) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/IDF.html)
- RegexTokenizer [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#tokenizer) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/RegexTokenizer.html)
- Tokenizer [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#tokenizer) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/Tokenizer.html)
- Word2Vec [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-features.html#word2vec) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/feature/Word2Vec.html)



### *Tuner*

- CrossValidator [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-tuning.html#cross-validation) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/tuning/CrossValidator.html)
- TrainValidationSplit [Spark MLlib example](https://spark.apache.org/docs/3.1.2/ml-tuning.html#train-validation-split) and [Documontation](https://spark.apache.org/docs/3.1.2/api/scala/org/apache/spark/ml/tuning/TrainValidationSplit.html)


### *Evaluator*

- RegressionEvaluator [Documontation](https://spark.apache.org/docs/latest/mllib-evaluation-metrics.html)
- MulticlassClassificationEvaluator [Documontation](https://spark.apache.org/docs/latest/mllib-evaluation-metrics.html)


## *CamaML schema*
![alt text](PA.PNG?raw=true)


## *Example*

For practical example you can refer to this [Link](https://github.com/jsarni/CaraMLTest), which is a github project that contain a project using the CaraML framework.

