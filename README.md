<!--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->

Apache Directory Build Tools 
============================

This repository contains build tools of the [Apache Directory project](https://directory.apache.org/).


Release Process
---------------

### Prepare the POM

    mvn release:prepare -DdryRun=true

### Deploy a snapshot

    mvn deploy

This is useful to verify your settings in ~/.m2/settings.xml (Nexus password and GPG key)

### Prepare the release

    mvn release:clean
    mvn release:prepare

When asked for the SCM release tag please remove the project prefix, the version number is enough.

This creates a tag here: <https://gitbox.apache.org/repos/asf?p=directory-buildtools.git>

### Stage the release

    mvn release:perform

This deploys the POM to a staging repository. Go to <https://repository.apache.org/index.html#stagingRepositories> and close the staging repository.

### Call the vote

Start the vote.

### Publish

After successful vote publish the artifacts, therefore go to <https://repository.apache.org/index.html#stagingRepositories> and release the staging repository.

