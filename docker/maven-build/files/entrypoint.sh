#!/bin/bash

# Stop execution if any command fails (i.e. exits with status code > 0)
set -e

# Inject username with dynamic uid/gid
echo "hnelson:x:$(id -u):$(id -g)::/home/hnelson:/bin/bash" >> /etc/passwd
echo "hnelson:x:$(id -g):" >> /etc/group
export HOME=/home/hnelson

# Start the Xvfb server (required for Studio UI tests)
export DISPLAY=:99
Xvfb :99 -screen 0 1024x768x16 &

# Limit memory usage (requried for Java 8 which is not Docker aware)
export MAVEN_OPTS="-Xmx1024m"
export _JAVA_OPTIONS="-Xmx1024m"

exec "$@"
