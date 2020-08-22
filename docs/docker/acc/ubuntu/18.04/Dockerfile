# ACC game server running using Wine, latest stable version from WineHQ

FROM ubuntu:18.04

MAINTAINER John Walsh <pringlez@github.com>

ENV DEBIAN_FRONTEND=noninteractive
ENV WINEDEBUG -all

# Game Server User, Files & Permissions
RUN mkdir -p /home/server/acc /home/server/config
COPY required/* /home/server/acc/
WORKDIR /home/server/acc

# Install System Updates & Packages
RUN apt-get update && apt-get install wget gnupg2 software-properties-common -y
RUN dpkg --add-architecture i386
RUN wget -qO- https://dl.winehq.org/wine-builds/winehq.key | apt-key add -
RUN apt-add-repository 'deb http://dl.winehq.org/wine-builds/ubuntu/ bionic main'
RUN add-apt-repository ppa:cybermax-dexter/sdl2-backport -y
RUN apt-get update
RUN apt-get install --install-recommends winehq-stable -y
RUN apt-get autoclean -y && wine --version

# Volume
VOLUME /home/server/config

# Starts ACC Game Server
ENTRYPOINT ["/bin/bash", "-c", "sh run-server.sh"]
