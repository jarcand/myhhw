#!/bin/sh

DIR="$( cd "$( dirname "$0" )" && pwd )"
cd $DIR

DATE=`date +%Y%m%d`
FILE="./logs/output.$DATE.log"

date --rfc-3339=seconds >> $FILE
java -cp bin/ ca.ariselab.myhhw.MyHHW hhwlogs/output.$DATE.log >> $FILE 2>&1 &
