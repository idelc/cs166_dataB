#!/bin/bash
export DB_NAME=$USER"_DB"
DIR="$( cd "$( dirname "$BASH_SOURCE[0]}" )" && pwd)"
# Indicate the path of the java compiler to use
export JAVA_HOME=/usr/csshare/pkgs/jdk1.7.0_17
export PATH=$JAVA_HOME/bin:$PATH

# Export classpath with the postgressql driver
export CLASSPATH=$CLASSPATH:$PWD/../lib/pg73jdbc3.jar

# compile the java program
javac -d $DIR/../classes $DIR/../src/ProfNetwork.java

#run the java program
#Use your database name, port number and login
java ProfNetwork $DB_NAME $PGPORT $USER


