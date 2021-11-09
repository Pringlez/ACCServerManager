#!/bin/bash
rm -r /home/accserver/cfg/current
cp -r /home/accserver/host/cfg/* /home/accserver/cfg
wine ./host/executable/accServer.exe
exec "$@"