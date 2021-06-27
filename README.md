# CaraML

## Presentation

CaraMl is a Scala Spark framework for distributed Machine Learning programs, using the Spark MLlib in the simplest possible way. No need to write hundreds or thousands lines code, just discribing pipline of models and/or transformations. the purpose is to do "Machine Learning as Code" 



## Requirements

To use CaraML framework, you must satisfy the following requirements:

- Scala version >= 2.12.13
- Spark version >= 3.1
- CaraML library


## Installation 

  - Spark :  https://spark.apache.org/downloads.html
  - Scala : https://www.scala-lang.org/download/
  - CaraML library : "Link to the CaraML Library"



## Usage

To use CaraML, you can add the framework dependency in your Spark application
- Sbt
  

    libraryDependencies += "https://github.com/jsarni/CaraML" %% "CaraML" % "0.1"

- Gradle


    compile group: 'https://github.com/jsarni/CaraML', name: 'CaraML', version: '0.1'

- Maven


    <dependency>
    <groupId>https://github.com/jsarni/CaraML</groupId>
    <artifactId>CaraML</artifactId>
    <version>0.1</version>
    </dependency>

CaraML needs the following information 
- Dataset path that will be used to transform ad train models
- Path where to save the final trained model and its metrics
- Master Spark URL (or local one)
- Path of the CaraYaml file, where the user will declare and set the pipeline with stages of SparkML models and/or SparkML transformations

The Yaml file will be used to describe a pipeline of stages, each stage could be a SparkML model or a Spark ML method of data preprocessing.
All CaraYaml files must start with "CaraPipeline:" keyword





## Available models



## Example

