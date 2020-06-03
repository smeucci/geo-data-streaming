# delete kafka stream app folder
rm -rf /tmp/kafka-streams/geo-data-kafka-streams

# delete kafka connect folder
rm -rf geo-data-kafka-connect/.tmp-connect

# create mongo geo data topic
docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --create --topic mongo.geo-data-streaming.geoData --partitions 3 --replication-factor 1

# create northern hemisphere geo data topic
docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --create --topic northern.hemisphere.geo.data --partitions 3 --replication-factor 1

# create southern hemisphere geo data topic
docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --create --topic southern.hemisphere.geo.data --partitions 3 --replication-factor 1

# create hemisphere geo data statistics topic
docker exec -it kafka kafka-topics --zookeeper zookeeper:2181 --create --topic hemisphere.geo.data.statistics --partitions 3 --replication-factor 1