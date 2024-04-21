#!/bin/bash

# Tests the case where an assignment asks for more counters than available
# and once more counters are added, the assignment is completed

chmod u+x client/src/test/resources/testInit.sh
./client/src/test/resources/testInit.sh "$@"

cd server/target/tpe1-g6-server-1.0-SNAPSHOT
chmod u+x run-server.sh

./run-server.sh > /dev/null 2>&1 & server_pid=$!
sleep 1

cd ../../../client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh
chmod u+x counterClient.sh

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=A
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=A -Dcounters=2
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=2

./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/test/resources/counterTester/assignCounters/bookings.csv

./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA123|AA124|AA125' -Dairline=AmericanAirlines -DcounterCount=3

./counterClient.sh -DserverAddress=localhost:50051 -Daction=listPendingAssignments -Dsector=C

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3

./counterClient.sh -DserverAddress=localhost:50051 -Daction=listPendingAssignments -Dsector=C

pkill -P "$server_pid"