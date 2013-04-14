#!/bin/sh

DIR="$( cd "$( dirname "$0" )" && pwd )"
cd $DIR

MSG='Restarted MyHHW throught command-line shell.'
if [ -n "$1" ]; then
	MSG=$1
fi

PID=`ps -ef | grep java | grep ca.ariselab.myhhw.MyHHW | awk '{print $2}'`
if [ -n "$PID" ]; then
	kill $PID
fi

if [ "$1" != "stop" ]; then
	
	DATE=`date +%Y%m%d`
	FILE="./logs/output.$DATE.log"
	
	TIME=`date +'%Y-%m-%d %H:%M:%S'`
	
	echo $TIME '- INFO - SHELL - ' $MSG >> $FILE
	
	DISPLAY=:0 ./start.sh &
fi
