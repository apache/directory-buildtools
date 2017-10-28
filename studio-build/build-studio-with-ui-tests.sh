#!/bin/bash

# stop execution if any command fails (i.e. exits with status code > 0)
set -e

# trace commands
set -x

cd /home/hnelson/studio
mvn -V -f pom-first.xml clean install
mvn -V clean install -Denable-ui-tests

