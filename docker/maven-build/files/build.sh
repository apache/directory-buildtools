#!/bin/bash

# Stop execution if any command fails (i.e. exits with status code > 0)
set -e

# Trace commands
set -x

# Default Maven build command
mvn -V clean install
