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

###########################################################
# RESULTADO ESPERADO EN expectedOutPut.txt                #
###########################################################

./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/main/resources/oneBooking.csv

./eventsClient.sh -DserverAddress=localhost:50051 -Daction=register -Dairline=AmericanAirlines > /tmp/eventsResults.txt & event=$!


chmod u+x ../../src/test/resources/EventsTester/scripts/TaskAirline.sh
./../../src/test/resources/EventsTester/scripts/TaskAirline.sh > /dev/null 2>&1 & task=$!

wait "$task"

diff /tmp/eventsResults.txt ../../src/test/resources/EventsTester/expected/expectedOutPut.txt

rm /tmp/eventsResults.txt

pkill -P "$event"
pkill -P "$server_pid"