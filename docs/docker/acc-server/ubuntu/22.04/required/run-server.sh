#!/bin/bash
# Remove existing configs used by acc server
rm -r /accserver/servers/accmanager/instances/$SERVER_INSTANCE/cfg/current
# Start acc server using wine
cd /accserver/servers/accmanager/instances/$SERVER_INSTANCE
wine ./accServer.exe
exec "$@"