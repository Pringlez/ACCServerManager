#!/bin/bash
# Remove existing configs used by acc server
rm -r /accserver/servers/accmanager/instances/$SERVER_INSTANCE/cfg/current
# Copy configs from host volume to working config directory
#cp -r /servers/host/cfg/* /servers/$SERVER_INSTANCE/cfg
# Start acc server using wine
wine ./accserver/servers/accmanager/instances/$SERVER_INSTANCE/accServer.exe
exec "$@"