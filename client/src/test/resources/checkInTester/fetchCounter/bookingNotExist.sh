#!/bin/bash

chmod u+x client/src/test/resources/testInit.sh

./client/src/test/resources/testInit.sh "$@"

cd client/target/tpe1-g6-client-1.0-SNAPSHOT/

chmod u+x passengerClient.sh

./passengerClient.sh -DserverAddress=localhost:50051 -Daction=fetchCounter -Dbooking=XYZ345