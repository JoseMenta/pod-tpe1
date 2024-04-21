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
./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/test/resources/counterTester/listCounters/bookings.csv >/dev/null

chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA123|AA124' -Dairline=AmericanAirlines -DcounterCount=3 >/dev/null
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=A -Dflights='AC987' -Dairline=AirCanada -DcounterCount=3 >/dev/null
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights='AA125' -Dairline=AmericanAirlines -DcounterCount=3 >/dev/null

chmod u+x passengerClient.sh
./passengerClient.sh -DserverAddress=localhost:50051 -Daction=passengerCheckin -Dbooking=XYZ234 -Dsector=C -Dcounter=1 >/dev/null
./passengerClient.sh -DserverAddress=localhost:50051 -Daction=passengerCheckin -Dbooking=DEF345 -Dsector=C -Dcounter=1 >/dev/null
./passengerClient.sh -DserverAddress=localhost:50051 -Daction=passengerCheckin -Dbooking=ABC123 -Dsector=A -Dcounter=4 >/dev/null

chmod u+x queryClient.sh

>/tmp/test_results
./queryClient.sh -DserverAddress=localhost:50051 -Daction=counters -DoutPath=/tmp/test_results -Dsector=C

cat /tmp/test_results
rm /tmp/test_results

pkill -P "$server_pid"