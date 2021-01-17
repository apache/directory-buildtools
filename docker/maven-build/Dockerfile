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

ARG JDK_VERSION=8
FROM maven:3-openjdk-${JDK_VERSION}-slim

# Base images maven:3-jdk-8 and maven:3-jdk-11 are based on Debian
# Base images maven:3-adoptopenjdk-* are based on Debian
# Base images maven:3-openjdk-*-slim are based on Debian
RUN \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends libgtk-3-0 libxtst6 xvfb krb5-config krb5-user dpkg rpm nsis && \
    rm -rf /var/lib/apt/lists/*

# Create the temp dir for Xvfb
RUN mkdir -p /tmp/.X11-unix && chmod 1777 /tmp/.X11-unix

# Make /etc/passwd writeable to be able to inject username with dynamic uid/gid
RUN chmod 666 /etc/passwd
RUN chmod 666 /etc/group

# Add krb5.conf with EXAMPLE.COM domain
ADD files/krb5.conf /etc/

# Create home directory
RUN mkdir -p /home/hnelson/project && chmod 777 /home/hnelson && chmod 777 /home/hnelson/project
WORKDIR /home/hnelson/project

# Add and configure entrypoint script
ADD files/entrypoint.sh /usr/local/bin
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

# Add build script and make it the default command
ADD files/build.sh /usr/local/bin
CMD "/usr/local/bin/build.sh"

