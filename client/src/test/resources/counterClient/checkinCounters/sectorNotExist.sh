#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh
./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/main/resources/testCheckInbookings.csv

chmod u+x counterClient.sh

./counterClient.sh -DserverAddress=localhost:50051 -Daction=checkinCounters -Dsector=C -DcounterFrom=3 -Dairline=AirCanada