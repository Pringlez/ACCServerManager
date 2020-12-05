#!/bin/bash
rm -r /home/server/accmanager/cfg/current
cp -r /home/server/accmanager/host/cfg/* /home/server/accmanager/cfg
wine ./host/executable/accServer.exe
exec "$@"