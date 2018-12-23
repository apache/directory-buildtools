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
FROM maven:3-jdk-${JDK_VERSION}

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update
RUN apt-get install -y --no-install-recommends xvfb
RUN apt-get install -y --no-install-recommends krb5-config krb5-user

# Create home directory
RUN mkdir /home/hnelson && chmod 777 /home/hnelson
WORKDIR /home/hnelson

# Add krb5.conf with EXAMPLE.COM domain
ADD krb5.conf /etc/

# Make /etc/passwd writeable to be able to inject username with dynamic uid/gid
RUN chmod 666 /etc/passwd

# Add and configure entrypoint script
ADD entrypoint.sh /usr/local/bin
RUN chmod 755 /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

# Add build script and make it the default command
ADD build-studio-with-ui-tests.sh /usr/local/bin
RUN chmod 755 /usr/local/bin/build-studio-with-ui-tests.sh
CMD "/usr/local/bin/build-studio-with-ui-tests.sh"

