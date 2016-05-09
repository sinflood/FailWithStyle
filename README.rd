This is a tool for analyzing the error handling style of Java code for author-related style traits. Check out the full report (final_report.pdf) for more information.

To build:
Build the dependencies with Maven (eclipse or command line)
javac ErrorController.java

To execute:
java ErrorController <path to dataset>
The main class is ErrorController. It takes one argument, the location of the dataset to be examined. There are also a few switches hardcoded into the class to adjust execution.

Dependencies:
Apache Spark
javaparser
