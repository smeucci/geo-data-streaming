#!/bin/sh

# sleep for 10 seconds waiting for kafka setup
echo "Waiting for 10 seconds..."
sleep 10

# start the app
echo "Starting geo-data-kafka-streams..."
java -jar ./geo-data-kafka-streams.jar