# Implementing Outbox Pattern

<TODO-Details...\>

### Sample requests
`curl -X POST http://localhost:8080/api/parcel -H  "Content-Type:application/json"  -d '{"contents":"Kotlin is cool"}'`

### Useful commands
1. ` ./gradlew flywayRepair`

## Kafka

```
docker exec -t kafka /usr/bin/kafka-topics \
      --create --bootstrap-server :9092 \
      --topic parcel_created \
      --partitions 1 \
      --replication-factor 1

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @kafka-connectors/dbz-source-connector.json
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @kafka-connectors/es-sink-connector.json
curl -X DELETE http://localhost:8083/connectors/elasticsearch-sink
```

### - Consuming Kafka Topics
```shell
docker exec -t kafka /usr/bin/kafka-console-consumer \
      --bootstrap-server :9092 \
      --property print.key=true \
      --from-beginning \
      --topic parcel_created
 