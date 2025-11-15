# Notification Service

Consumes achat events from Kafka and stores them as Notification records for later querying.

## Event Schema
```
{
  "achatId": Long,
  "userId": Long,
  "userName": String,
  "ticketId": Long,
  "ticketDescription": String,
  "trajetId": Long,
  "nomTrajet": String,
  "cityName": String,
  "priceInDhs": Number,
  "createdAt": "ISO or array from Jackson" // achat creation time
}
```

## REST Endpoints
- `GET /notifications` – list all stored notifications
- `GET /notifications/{id}` – retrieve single notification

## Configuration
| Property | Description | Default |
|----------|-------------|---------|
| `spring.kafka.bootstrap-servers` | Kafka broker address | `broker:9092` |
| `app.kafka.topics.achat-created` | Achat event topic | `ticket` |
| `spring.kafka.consumer.group-id` | Consumer group | `notification-service-group` |

## Run Locally (H2)
```bash
mvn spring-boot:run -pl notification-service
```

## Override to Postgres
Set `DATASOURCE_URL`, `DATASOURCE_USERNAME`, `DATASOURCE_PASSWORD` env vars and ensure `spring.jpa.hibernate.ddl-auto=update`.

## Next Improvements
- Add filtering by userId/trajetId
- Add receivedAt index for cleanup/retention
- Add dead-letter handling for failed events.
