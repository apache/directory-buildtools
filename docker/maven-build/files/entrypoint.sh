#
#   Licensed to the Apache Software Foundation (ASF) under one 
#   or more contributor license agreements.  See the NOTICE file
#   distributed with this work for additional information
#   regarding copyright ownership.  The ASF licenses this file
#   to you under the Apache License, Version 2.0 (the
#   "License"); you may not use this file except in compliance
#   with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing,
#   software distributed under the License is distributed on an
#   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
#   KIND, either express or implied.  See the License for the 
#   specific language governing permissions and limitations
#   under the License.
#
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
