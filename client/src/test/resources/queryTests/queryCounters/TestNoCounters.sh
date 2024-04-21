#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd server/target/tpe1-g6-server-1.0-SNAPSHOT
chmod u+x run-server.sh

./run-server.sh > /dev/null 2>&1 & server_pid=$!
sleep 1

cd ../../../client/target/tpe1-g6-client-1.0-SNAPSHOT/


chmod u+x queryClient.sh

./queryClient.sh -DserverAddress=localhost:50051 -Daction=counters -DoutPath=/tmp/test_results
cat /tmp/test_results

pkill -P "$server_pid"