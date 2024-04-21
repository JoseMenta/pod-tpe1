#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh
./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/main/resources/testCheckInbookings.csv
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3

chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AC987'|'AD987'|'AE987 -Dairline=AirCanada -DcounterCount=20

chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listPendingAssignments -Dsector=C