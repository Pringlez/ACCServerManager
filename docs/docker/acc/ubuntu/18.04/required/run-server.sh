#!/bin/bash
cp -rf /home/server/config/* /home/server/acc/
wine ./accServer.exe
exec "$@"
