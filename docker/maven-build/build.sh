#!/bin/bash

# stop execution if any command fails (i.e. exits with status code > 0)
set -e

# trace commands
set -x

# Temporary workaround with Surefire on Debian with Java 8 and limit memory usage
# https://issues.apache.org/jira/browse/SUREFIRE-1588
# https://stackoverflow.com/questions/53010200/maven-surefire-could-not-find-forkedbooter-class
export MAVEN_OPTS="-Xmx1024m"
export _JAVA_OPTIONS="-Djdk.net.URLClassPath.disableClassPathURLCheck=true -Xmx1024m"

mvn -V clean install

