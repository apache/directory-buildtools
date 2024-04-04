FROM ubuntu:22.04

MAINTAINER Olivier Lamy olamy@apache.org
ARG MAVEN_VERSION=3.8.6
ARG USER_HOME_DIR="/root"

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

#non interactive
ENV DEBIAN_FRONTEND=noninteractive

# Packages Update / Install

USER root

RUN                                         \
  apt -q update                             \
  && apt -q upgrade -y                      \
  && apt -q install -y                      \
                libgtk-3-0                  \
                libxtst6                    \
                curl                        \
                git                         \ 
                tar                         \
                xvfb                        \
                iputils-ping                \
                ldap-utils                  \
                libsasl2-modules-ldap       \
                libsasl2-modules-gssapi-mit \
                krb5-config                 \
                krb5-user                   \
                dpkg                        \
                rpm                         \
                nsis                        \
  && apt -q autoremove                      \
  && apt -q clean -y                        \
  && mkdir -p /tmp/.X11-unix                \
  && chmod 1777 /tmp/.X11-unix              \
  && chmod 666 /etc/passwd                  \
  && chmod 666 /etc/group                   \
  && mkdir -p /home/hnelson/project         \
  && chmod 777 /home/hnelson                \
  && chmod 777 /home/hnelson/project

# Add krb5.conf with EXAMPLE.COM domain
ADD files/krb5.conf /etc/

# Create home directory
WORKDIR /home/hnelson/project

# Add and configure entrypoint script
ADD files/entrypoint.sh /usr/local/bin
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

# too heavy
#COPY --from=eclipse-temurin:8u345-b01-jdk /opt/java/openjdk /opt/java/jdk8
#COPY --from=eclipse-temurin:11.0.16.1_1-jdk /opt/java/openjdk /opt/java/jdk11
#COPY --from=eclipse-temurin:17.0.4.1_1-jdk /opt/java/openjdk /opt/java/jdk17
#COPY --from=eclipse-temurin:19_36-jdk /opt/java/openjdk /opt/java/jdk19
COPY --from=eclipse-temurin:20_36-jdk /opt/java/openjdk /opt/java/jdk20

ENV JAVA_HOME=/opt/java/jdk20
ENV PATH="${JAVA_HOME}/bin:${PATH}"

ARG BASE_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn \
  && mkdir /home/jenkins \
  && mkdir /home/jenkins/agent

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000

RUN \
  addgroup -gid ${gid} ${group} \
  && adduser --home /home/${user} --uid ${uid} --gid ${gid} ${user}

ARG AGENT_WORKDIR=/home/${user}/agent

ENV AGENT_WORKDIR=${AGENT_WORKDIR}
RUN mkdir /home/${user}/.jenkins \
  && mkdir -p ${AGENT_WORKDIR}
VOLUME /home/${user}/.jenkins
VOLUME ${AGENT_WORKDIR}
WORKDIR /home/${user}

RUN cd /home/jenkins/ \
  && git clone --mirror https://github.com/apache/directory-ldap-api.git

RUN cd /usr/local/bin/ \
  && echo "#!/usr/bin/env sh" > startSlave.sh \
  && echo "kubedock server --lock --namespace=kubedock --timeout=10m0s --request-cpu=1 --request-memory=2Gi --port-forward &" >> startSlave.sh \
  && echo "/usr/local/bin/jenkins-agent" >> startSlave.sh \
  && chmod +x startSlave.sh \
  && chown -R jenkins:jenkins /home/jenkins

ENV JAVA_HOME=/usr

# Add build script and make it the default command
ADD files/build.sh /usr/local/bin

CMD "/usr/local/bin/build.sh"




