#!/bin/sh
export DB_NAME=$USER"_DB"
# Indicate the path of the java compiler to use
export JAVA_HOME=/usr/csshare/pkgs/jdk1.7.0_17
export PATH=$JAVA_HOME/bin:$PATH

# Export classpath with the postgressql driver
export CLASSPATH=$CLASSPATH:$PWD/pg73jdbc3.jar

# compile the java program
javac ProfNetwork.java

#run the java program
#Use your database name, port number and login
java ProfNetwork $USER"_DB" $PGPORT $USER

