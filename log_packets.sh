#!/bin/sh

DIR=`dirname $0`

while true; do
    java -cp "$DIR/bin:$DIR/lib/javAX25lib.jar" -Dinput="$APRS_INPUT" net.bberger.packetlogger.text.PacketLogger
done


