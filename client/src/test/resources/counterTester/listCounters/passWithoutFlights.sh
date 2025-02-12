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
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=B >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3 >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=A -Dcounters=3 >/dev/null
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=3 >/dev/null

chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listCounters -Dsector=C -DcounterFrom=1 -DcounterTo=3
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listCounters -Dsector=C -DcounterFrom=1 -DcounterTo=9
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listCounters -Dsector=C -DcounterFrom=4 -DcounterTo=9
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listCounters -Dsector=C -DcounterFrom=10 -DcounterTo=12
./counterClient.sh -DserverAddress=localhost:50051 -Daction=listCounters -Dsector=B -DcounterFrom=1 -DcounterTo=9


pkill -P "$server_pid"