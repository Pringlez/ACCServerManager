# ACC game server running using Wine, latest stable version from WineHQ

FROM ubuntu:20.04

MAINTAINER John Walsh <pringlez@github.com>

ENV DEBIAN_FRONTEND=noninteractive
ENV WINEDEBUG -all

# Game Server User, Files & Permissions
#RUN groupadd -r accserver && useradd -r -d /home/accserver -g accserver accserver
#RUN mkdir -p /opt/accserver/cfg
COPY required/run-server.sh /accserver/script/
#WORKDIR /home/accserver
#RUN chown -R accserver:accserver /home/accserver
#RUN chmod -R 777 /home/accserver

# Install System Updates & Packages
RUN dpkg --add-architecture i386
RUN apt-get update && apt-get install wget gnupg2 software-properties-common -y
RUN wget -qO- https://dl.winehq.org/wine-builds/winehq.key | apt-key add -
RUN apt-add-repository 'deb http://dl.winehq.org/wine-builds/ubuntu/ focal main'
RUN apt-get install -y --install-recommends winehq-staging
RUN apt-get autoclean -y && wine --version

# Volume
#VOLUME /accmanager

WORKDIR /accserver

# Starts ACC Game Server
#USER accserver
#ENTRYPOINT ["/bin/bash", "-c", "sh /accserver/script/run-server.sh"]

# Debug Container
ENTRYPOINT ["tail", "-f", "/dev/null"]