scalaVersion := "2.12.13"

name := "CaraML"

version := "1.0.0"

organization := "io.github.jsarni"
homepage := Some(url("https://github.com/jsarni/CaraML"))
scmInfo := Some(ScmInfo(url("https://github.com/jsarni/CaraML"), "git@github.com:jsarni/CaraML.git"))
developers :=
  List(
    Developer("Juba", "SARNI", "juba.sarni@gmail.com", url("https://github.com/jsarni")),
    Developer("Merzouk", "OUMEDDAH", "merzoukoumeddah@gmail.com ", url("https://github.com/merzouk13")),
    Developer("Aghylas", "SAI", "aghilassai@gmail.com", url("https://github.com/SAI-Aghylas"))
  )

// Dependencies
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7" % Test
val mockito = "org.mockito" %% "mockito-scala" % "1.16.37" % Test
val spark = "org.apache.spark" %% "spark-mllib" % "3.1.1"

lazy val caraML = (project in file("."))
  .settings(
    name := "CaraML",
    libraryDependencies += scalaTest,
    libraryDependencies += mockito,
    libraryDependencies += spark
  )