#!/usr/bin/env bash

# create tmp folder for connect offsets
mkdir -p .tmp-connect

# run the mongo source connector
connect-standalone.sh connect-standalone.properties mongo-source-connector.properties
