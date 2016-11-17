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
* Java 8
* Maven 3
* Xvfb (for running UI tests)
* LDAP client
* Kerberos client


## Build image

    docker build -t apachedirectory/studio-build .


## Publish image

    docker push apachedirectory/studio-build


## Usage

    PATH_TO_STUDIO_SRC=...
    docker run -it --rm \
        -u $(id -u):$(id -g) \
        -e HOME=/home/studio \
        -v ~/.m2:/home/studio/.m2 \
        -v $PATH_TO_STUDIO_SRC:/home/studio/src \
        apachedirectory/studio-build bash

    cd /home/studio/src
    mvn clean install -Denable-ui-tests


