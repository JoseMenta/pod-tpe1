#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd server/target/tpe1-g6-server-1.0-SNAPSHOT
chmod u+x run-server.sh

./run-server.sh > /dev/null 2>&1 & server_pid=$!
sleep 1

cd ../../../client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=A >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3 >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=A -Dcounters=3 >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3 >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/test/resources/counterClient/assignCounters/bookings.csv >/dev/null

chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA123|AA124|AA125' -Dairline=AmericanAirlines -DcounterCount=2 >/dev/null
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listCounters -Dsector=C -DcounterFrom=1 -DcounterTo=9

pkill -P "$server_pid"