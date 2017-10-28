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

FROM alpine

RUN apk add --no-cache openldap openldap-back-hdb openldap-clients openssl

ADD slapd-config.ldif slapd-data.ldif /tmp/

# Generate a self-signed SSL certificate
RUN openssl req -newkey rsa:4096 -sha512 -x509 -days 3650 -nodes \
    -subj "/C=NA/ST=NA/L=NA/O=NA/CN=example.com" \
    -out /etc/ssl/certs/ldap.pem -keyout /etc/ssl/private/ldap.pem && \
    chown root:ldap /etc/ssl/private/ldap.pem && \
    chmod 640 /etc/ssl/private/ldap.pem

# Inject configuration
RUN mkdir /etc/openldap/slapd.d && \
    slapadd -d -1 -F /etc/openldap/slapd.d -n 0 -l /tmp/slapd-config.ldif

# Inject data
RUN slapadd -d 1 -F /etc/openldap/slapd.d -l /tmp/slapd-data.ldif

# Fix permissions
RUN chown -R ldap:ldap /var/lib/openldap && \
    chown -R ldap:ldap /etc/openldap/slapd.d

EXPOSE 389 636

CMD /usr/sbin/slapd -d 256 -u ldap -g ldap -F /etc/openldap/slapd.d -h "ldap:/// ldaps:///"

