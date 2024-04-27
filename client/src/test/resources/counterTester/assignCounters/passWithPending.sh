#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd server/target/tpe1-g6-server-1.0-SNAPSHOT
chmod u+x run-server.sh

./run-server.sh > /dev/null 2>&1 & server_pid=$!
sleep 1

cd ../../../client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C > /dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=20
./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/test/resources/counterTester/assignCounters/bookings.csv

chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listPendingAssignments -Dsector=C
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA123' -Dairline=AmericanAirlines -DcounterCount=10
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA124' -Dairline=AmericanAirlines -DcounterCount=100
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listPendingAssignments -Dsector=C
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA125' -Dairline=AmericanAirlines -DcounterCount=5

./counterClient.sh -DserverAddress=localhost:50051 -Daction=listPendingAssignments -Dsector=C


pkill -P "$server_pid"