#!/bin/bash

if [[ '-b' == "$1" ]]; then
    echo "Building the project"

    mvn clean package > /dev/null

    cd client/target/

    tar -xzf tpe1-g6-client-1.0-SNAPSHOT-bin.tar.gz
else
    cd client/target/

    echo "Project was not built"
fi

cd tpe1-g6-client-1.0-SNAPSHOT/