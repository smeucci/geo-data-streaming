# delete kafka stream app folder
echo "deleting kafka streams app folder..."
rm -rf /tmp/kafka-streams/geo-data-kafka-streams

# delete kafka connect folder
echo "deleting kafka connect folder..."
rm -rf geo-data-kafka-connect/.tmp-connect

# create mongo geo data topic
echo "creating mongo geo data topic..."
docker exec -it kafka-geo-data-streaming kafka-topics --zookeeper zookeeper-geo-data-streaming:2181 --create --topic mongo.geo-data-streaming.geoData --partitions 3 --replication-factor 1

# create northern hemisphere geo data topic
echo "creating northern hemisphere geo data topic..."
docker exec -it kafka-geo-data-streaming kafka-topics --zookeeper zookeeper-geo-data-streaming:2181 --create --topic northern.hemisphere.geo.data --partitions 3 --replication-factor 1

# create southern hemisphere geo data topic
echo "creating southern hemisphere geo data topic..."
docker exec -it kafka-geo-data-streaming kafka-topics --zookeeper zookeeper-geo-data-streaming:2181 --create --topic southern.hemisphere.geo.data --partitions 3 --replication-factor 1

# create hemisphere geo data statistics topic
echo "creating hemisphere geo data statistics topic..."
docker exec -it kafka-geo-data-streaming kafka-topics --zookeeper zookeeper-geo-data-streaming:2181 --create --topic hemisphere.geo.data.statistics --partitions 3 --replication-factor 1 \
--config cleanup.policy=compact \
--config min.cleanable.dirty.ratio=0.005 \
--config segment.ms=10000