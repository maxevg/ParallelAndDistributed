hadoop fs -rmr output
mvn package
hadoop fs -copyFromLocal 664600583_T_ONTIME_sample.csv
hadoop fs -copyFromLocal L_AIRPORT_ID.csv
export HADOOP_CLASSPATH=target/hadoop-examples-1.0-SNAPSHOT.jar
hadoop FlightJob 664600583_T_ONTIME_sample.csv output
hadoop fs -copyToLocal output
exit(0)