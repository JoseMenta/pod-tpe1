#!/bin/bash

chmod u+x adminClient.sh
./adminClient.sh -DserverAddress=localhost:50051 -Daction=manifest -DinPath=../../src/main/resources/testNotification.csv

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=A
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=A -Dcounters=2

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C
./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=2


chmod u+x counterClient.sh
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AA123'|'AA124'|'AA125 -Dairline=AmericanAirlines -DcounterCount=2


chmod u+x passengerClient.sh
./passengerClient.sh -DserverAddress=localhost:50051 -Daction=passengerCheckin -Dbooking=ABC123 -Dsector=C -Dcounter=3
./passengerClient.sh -DserverAddress=localhost:50051 -Daction=passengerCheckin -Dbooking=ABC124 -Dsector=C -Dcounter=3


./counterClient.sh -DserverAddress=localhost:50051 -Daction=checkinCounters -Dsector=C -DcounterFrom=3 -Dairline=AmericanAirlines


./counterClient.sh -DserverAddress=localhost:50051 -Daction=freeCounters -Dsector=C -DcounterFrom=3 -Dairline=AmericanAirlines

./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AC122 -Dairline=AirCanada  -DcounterCount=2
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AC123 -Dairline=AirCanada  -DcounterCount=2
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AC124 -Dairline=AirCanada -DcounterCount=2
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AC125 -Dairline=AirCanada -DcounterCount=2
./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AC126 -Dairline=AirCanada -DcounterCount=2

./counterClient.sh -DserverAddress=localhost:50051 -Daction=assignCounters -Dsector=C -Dflights=AA888'|'AA999 -Dairline=AmericanAirlines -DcounterCount=7

./adminClient.sh -DserverAddress=localhost:50051 -Daction=addCounters -Dsector=C -Dcounters=2


