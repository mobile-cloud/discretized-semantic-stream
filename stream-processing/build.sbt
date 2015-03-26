name := "stream-processing"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.jena" % "jena-arq" % "2.11.1"

libraryDependencies += "org.apache.jena" % "jena-sdb" % "1.4.1"

libraryDependencies += "org.apache.jena" % "jena-tdb" % "1.0.1"

libraryDependencies += "org.slf4j" % "jcl-over-slf4j" % "1.6.4"

//libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.2.1"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "0.9.2"