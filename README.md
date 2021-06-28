# CaraML

## *Presentation*

CaraMl is a Scala Spark framework for distributed Machine Learning programs, using the Spark MLlib in the simplest possible way. No need to write hundreds or thousands lines code, just discribing pipline of models and/or transformations. the purpose is to do "Machine Learning as Code" 



## *Requirements*

To use CaraML framework, you must satisfy the following requirements:

- Scala version >= 2.12.13
- Spark version >= 3.1
- CaraML library


## *Installation* 

  - Spark :  [Download here](https://spark.apache.org/downloads.html) 
  - Scala : [Download here](https://www.scala-lang.org/download/)
  - CaraML library : [CaraML]()



## *Usage*

To use CaraML, you can add the framework dependency in your Spark application
- Sbt

```scala
  libraryDependencies += "https://github.com/jsarni/CaraML" %% "CaraML" % "0.1"
```

- Gradle

```scala
    compile group: 'https://github.com/jsarni/CaraML', name: 'CaraML', version: '0.1'
```
- Maven

```scala
    <dependency>
    <groupId>https://github.com/jsarni/CaraML</groupId>
    <artifactId>CaraML</artifactId>
    <version>0.1</version>
    </dependency>
```
CaraML needs the following information 
- Dataset that will be used to transform ad train models
- Path where to save the final trained model and its metrics
- Master Spark URL (or local one)
- Path of the CaraYaml file, where the user will declare and set the pipeline with stages of SparkML models and/or SparkML transformations

The Yaml file will be used to describe a pipeline of stages, each stage could be a SparkML model or a Spark ML method of data preprocessing.
All CaraYaml files must start with "CaraPipeline:" keyword and could contain the following keywords 

### *CaraPipeline*
* **"CaraPipeline:"** : keyword that must be set in the beginning of each CaraYaml 


### *Stage*
* **"- stage:"** Is the keyword used to declare and describe a stage. It could be an Estimator or a Transformer :
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
- evaluator: MulticlassClassificationEvaluator
- tuner: TrainValidationSplit
  params:
    - TrainRatio: 0.8


```

**For more details and documentation you can refer to the Spark [MLlib](https://spark.apache.org/docs/3.1.2/ml-guide.html) documentation**



## *Available models*

This section lists all available  



## *Example*

