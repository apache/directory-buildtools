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

A docker image for building Apache Directory projects. It's based on the official maven Docker images.

It contains all tools required for building and testing:
* Java (OpenJDK 8, 11, 17)
* Maven 3.8
* Xvfb (required by Studio UI tests)
* Kerberos client and krb5 config with EXAMPLE.COM realm (required by Studio UI tests)
* dpkg, rpm, nsis (for building ApacheDS installers)

## Build image

See <https://hub.docker.com/_/maven> for available Maven base image tags.

Currently used:
* 8: `maven:3-openjdk-8-slim`
* 11: `maven:3-openjdk-11-slim`
* 17: `maven:3-openjdk-17-slim`

```
JDK_VERSION=17
docker pull maven:3-openjdk-${JDK_VERSION}-slim
docker build -t apachedirectory/maven-build:jdk-${JDK_VERSION} --build-arg JDK_VERSION=${JDK_VERSION} .
```

## Publish image

```
docker push apachedirectory/maven-build:jdk-${JDK_VERSION}
```

## Usage

Local:

```
docker run -it --rm \
    -u $(id -u):$(id -g) \
    -v ~/.m2:/home/hnelson/.m2 \
    -v $(pwd):/home/hnelson/project \
    apachedirectory/maven-build:jdk-8 mvn -V clean verify
```

On Jenkins:

```
docker run -i --rm \
    -u $(id -u):$(id -g) \
    -v ~/.m2:/home/hnelson/.m2 \
    -v $(pwd):/home/hnelson/project \
    apachedirectory/maven-build:jdk-8 mvn -V clean verify
```

In Jenkins Pipeline:

```
    agent {
        docker {
            label 'ubuntu'
            image 'apachedirectory/maven-build:jdk-8'
            args '-v $HOME/.m2:/var/maven/.m2'
        }
    }
    steps {
         sh 'mvn -V clean verify'
    }
```
