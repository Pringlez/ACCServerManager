# ACC game server running using Wine, latest stable version from WineHQ

FROM ubuntu:20.04

MAINTAINER John Walsh <pringlez@github.com>

ENV DEBIAN_FRONTEND=noninteractive
ENV WINEDEBUG -all

# Game Server User, Files & Permissions
RUN groupadd -r server && useradd -r -d /home/server -g server server
RUN mkdir -p /home/server/accmanager /home/server/accmanager/cfg
COPY required/run-server.sh /home/server/accmanager/
WORKDIR /home/server/accmanager
RUN chown -R server:server /home/server
RUN chmod -R 777 /home/server

# Install System Updates & Packages
RUN dpkg --add-architecture i386
RUN apt-get update && apt-get install wget gnupg2 software-properties-common -y
RUN wget -qO- https://dl.winehq.org/wine-builds/winehq.key | apt-key add -
RUN apt-add-repository 'deb http://dl.winehq.org/wine-builds/ubuntu/ focal main'
RUN apt-get install -y --install-recommends winehq-staging
RUN apt-get autoclean -y && wine --version

# Volume
VOLUME /home/server/accmanager/host

# Starts ACC Game Server
USER server
ENTRYPOINT ["/bin/bash", "-c", "sh run-server.sh"]