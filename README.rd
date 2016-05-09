This is a tool for analyzing the error handling style of Java code for author-related style traits at scale. Check out the full report (final_report.pdf) for more information.

To build:
Build the dependencies with Maven (using pom.xml in either Eclipse or command line mvn)
javac ErrorController.java

To execute:
java ErrorController <path to dataset>

The main class is ErrorController. It takes one argument, the location of the dataset to be examined. There are also a few switches hardcoded into the class to adjust execution.

The evaluation dataset used in the report is also included with the project for verification purposes.

Dependencies:
Apache Spark (http://spark.apache.org/)
JavaParser (https://github.com/javaparser/javaparser)
