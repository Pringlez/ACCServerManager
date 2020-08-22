# ACC game server running using Wine, latest stable version from WineHQ

FROM ubuntu:20.04

MAINTAINER John Walsh <pringlez@github.com>

ENV DEBIAN_FRONTEND=noninteractive
ENV WINEDEBUG -all

# Game Server User, Files & Permissions
RUN mkdir -p /home/server/acc /home/server/config
COPY required/* /home/server/acc/
WORKDIR /home/server/acc

# Install System Updates & Packages
RUN dpkg --add-architecture i386
RUN apt-get update && apt-get install wget gnupg2 software-properties-common -y

RUN wget -qO- https://dl.winehq.org/wine-builds/winehq.key | apt-key add -
RUN apt-add-repository 'deb http://dl.winehq.org/wine-builds/ubuntu/ focal main'
RUN apt-get install -y --install-recommends winehq-staging
RUN apt-get autoclean -y && wine --version

# Volume
VOLUME /home/server/config

# Starts ACC Game Server
ENTRYPOINT ["/bin/bash", "-c", "sh run-server.sh"]
