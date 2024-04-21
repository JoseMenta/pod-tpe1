#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd server/target/tpe1-g6-server-1.0-SNAPSHOT
chmod u+x run-server.sh

./run-server.sh > /dev/null 2>&1 & server_pid=$!
sleep 1

cd ../../../client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh
chmod u+x eventsClient.sh



./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/main/resources/bookings.csv

./eventsClient.sh -DserverAddress=localhost:50051 -Daction=register -Dairline=AmericanAirlines & eventSuccess=$!
sleep 2
./eventsClient.sh -DserverAddress=localhost:50051 -Daction=register -Dairline=AmericanAirlines & eventFail=$!

pkill -P "$eventSuccess"
pkill -P "$eventFail"
pkill -P "$server_pid"