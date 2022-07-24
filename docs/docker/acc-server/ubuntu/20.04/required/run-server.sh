#!/bin/bash
# Remove existing configs used by acc server
rm -r /home/accserver/cfg/current
# Copy configs from host volume to working config directory
cp -r /home/accserver/host/cfg/* /home/accserver/cfg
# Start acc server using wine
wine ./host/executable/accServer.exe
exec "$@"