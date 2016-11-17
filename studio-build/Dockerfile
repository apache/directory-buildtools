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

FROM maven

RUN apt-get update
RUN apt-get install -y --no-install-recommends xvfb

RUN mkdir /home/studio && chmod 777 /home/studio
WORKDIR /home/studio

ADD entrypoint.sh /usr/local/bin
RUN chmod 755 /usr/local/bin/entrypoint.sh

ADD mvn /usr/local/bin
RUN chmod 755 /usr/local/bin/mvn

ADD krb5.conf /etc/

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD "/bin/bash"

