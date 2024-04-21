#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd server/target/tpe1-g6-server-1.0-SNAPSHOT
chmod u+x run-server.sh

./run-server.sh > /dev/null 2>&1 & server_pid=$!
sleep 1

cd ../../../client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x adminClient.sh

>/tmp/test_res.txt

#10 times
for i in {0..9}; do
    ./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/main/resources/bookings_larger.csv >> /tmp/test_res.txt &
    pids[${i}]=$!
done

# wait for all pids
for pid in ${pids[*]}; do
    wait $pid
done

cat /tmp/test_res.txt | grep successfully  | wc
cat /tmp/test_res.txt | grep already  | wc

rm /tmp/test_res.txt

pkill -P "$server_pid"