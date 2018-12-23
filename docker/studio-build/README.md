> Licensed to the Apache Software Foundation (ASF) under one
> or more contributor license agreements.  See the NOTICE file
> distributed with this work for additional information
> regarding copyright ownership.  The ASF licenses this file
> to you under the Apache License, Version 2.0 (the
> "License"); you may not use this file except in compliance
> with the License.  You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing,
> software distributed under the License is distributed on an
> "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
> KIND, either express or implied.  See the License for the
> specific language governing permissions and limitations
> under the License.


# About

A docker image to run Apache Directory Studio build included full test suite within a docker container.

It contains all requirements:
* OpenJDK 8 or 11
* Maven 3.6
* Xvfb (for running UI tests)
* LDAP client
* Kerberos client


## Build image

See <https://hub.docker.com/_/maven> for available Maven base image tags.

    JDK_VERSION=8
    docker pull maven:3-jdk-${JDK_VERSION}
    docker build -t apachedirectory/studio-build:jdk-${JDK_VERSION} --build-arg JDK_VERSION=${JDK_VERSION} .


## Publish image

    docker push apachedirectory/studio-build:jdk-${JDK_VERSION}


## Usage

Local:

    docker run -it --rm \
        -u $(id -u):$(id -g) \
        -v ~/.m2:/home/hnelson/.m2 \
        -v $(pwd):/home/hnelson/studio \
        apachedirectory/studio-build:jdk-8 bash

    cd /home/hnelson/studio
    mvn -f pom-first.xml clean install
    mvn clean install -Denable-ui-tests


On Jenkins:

    docker run -i --rm \
        -u $(id -u):$(id -g) \
        -v ~/.m2:/home/hnelson/.m2 \
        -v $(pwd):/home/hnelson/studio \
        apachedirectory/studio-build:jdk-8


