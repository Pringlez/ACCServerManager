# ACC game server running using Wine, latest stable version from WineHQ
FROM ubuntu:22.04

MAINTAINER John Walsh <pringlez@github.com>

ENV DEBIAN_FRONTEND=noninteractive
ENV WINEDEBUG -all

COPY required/run-server.sh /accserver/script/

# Install System Updates & Packages
RUN dpkg --add-architecture i386
RUN apt-get update && apt-get install wget gnupg2 software-properties-common -y
RUN wget -qO- https://dl.winehq.org/wine-builds/winehq.key | apt-key add -
RUN add-apt-repository 'deb https://dl.winehq.org/wine-builds/ubuntu/ impish main'
RUN apt-get update && apt-get install --install-recommends winehq-stable -y
RUN apt-get autoclean -y && wine --version

WORKDIR /accserver

# Starts ACC Game Server
ENTRYPOINT ["/bin/bash", "-c", "sh /accserver/script/run-server.sh"]

# Debug Container
#ENTRYPOINT ["tail", "-f", "/dev/null"]