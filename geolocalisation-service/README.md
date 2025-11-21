# Service de Géolocalisation - Bus Transport App

Service de géolocalisation en temps réel pour tracker les bus du réseau de transport.

## 🏗️ Architecture

### Technologies
- **Spring Boot** 3.5.6
- **Java** 21
- **PostgreSQL** 16 - Stockage de l'historique des positions
- **Redis** 7 - Cache de la dernière position
- **WebSocket (STOMP)** - Diffusion temps réel

### Modèle de Données

#### Bus
```java
{
  "id": Long,
  "matricule": String,
  "description": String,
  "trajetId": Long
}
```

#### Location
```java
{
  "id": Long,
  "bus": Bus,
  "latitude": Double,
  "longitude": Double,
  "timestamp": Instant
}
```

#### LocationDTO
```java
{
  "busId": Long,
  "latitude": Double,
  "longitude": Double,
  "timestamp": Long
}
```

## 🚀 Démarrage

### Avec Docker (Recommandé)
```bash
# Depuis la racine du projet
docker-compose up -d geolocalisation-service

# Vérifier les logs
docker-compose logs -f geolocalisation-service
```

### En développement local
```bash
# Prérequis: PostgreSQL et Redis doivent être démarrés
cd geolocalisation-service
./mvnw spring-boot:run
```

## 📡 API

### REST Endpoints

#### Sauvegarder une position
```http
POST /api/location
Content-Type: application/json

{
  "busId": 1,
  "latitude": 36.8065,
  "longitude": 10.1815,
  "timestamp": 1700000000000
}
```

**Réponse**: 200 OK

**Comportement**:
1. Valide que le bus existe
2. Sauvegarde dans PostgreSQL (historique)
3. Met à jour Redis (dernière position)
4. Diffuse via WebSocket au topic `/topic/bus-location`

#### Récupérer la dernière position
```http
GET /api/location/{busId}
```

**Réponse**:
```json
{
  "busId": 1,
  "latitude": 36.8065,
  "longitude": 10.1815,
  "timestamp": 1700000000000
}
```

**Source**: Redis (cache ultra-rapide)

### WebSocket

#### Connexion
```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // S'abonner aux positions
    stompClient.subscribe('/topic/bus-location', function(message) {
        const location = JSON.parse(message.body);
        console.log('Position reçue:', location);
        // Mettre à jour la carte...
    });
});
```

#### Topic
- **Topic**: `/topic/bus-location`
- **Format**: LocationDTO (JSON)
- **Fréquence**: À chaque nouvelle position reçue

## ⚙️ Configuration

### Variables d'environnement

| Variable | Description | Défaut |
|----------|-------------|--------|
| `SPRING_DATASOURCE_URL` | URL PostgreSQL | `jdbc:postgresql://localhost:5432/busdb` |
| `SPRING_DATASOURCE_USERNAME` | User PostgreSQL | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Password PostgreSQL | `postgres` |
| `SPRING_REDIS_HOST` | Host Redis | `localhost` |
| `SPRING_REDIS_PORT` | Port Redis | `6379` |
| `SERVER_PORT` | Port du service | `8080` |

### Configuration PostgreSQL
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/busdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Configuration Redis
```properties
spring.redis.host=localhost
spring.redis.port=6379
```

## 🗄️ Base de Données

### PostgreSQL

#### Table: bus
```sql
CREATE TABLE bus (
  id BIGSERIAL PRIMARY KEY,
  matricule VARCHAR(255),
  description VARCHAR(255),
  trajet_id BIGINT
);
```

#### Table: location
```sql
CREATE TABLE location (
  id BIGSERIAL PRIMARY KEY,
  bus_id BIGINT REFERENCES bus(id),
  latitude DOUBLE PRECISION,
  longitude DOUBLE PRECISION,
  timestamp TIMESTAMP
);
```

### Redis

**Structure**: 
```
Key: "bus:{busId}"
Value: LocationDTO (JSON)
TTL: Pas d'expiration (données en cache permanent)
```

**Exemple**:
```
Key: "bus:1"
Value: {"busId":1,"latitude":36.8065,"longitude":10.1815,"timestamp":1700000000000}
```

## 🧪 Tests

### Test manuel avec curl

```bash
# 1. Créer un bus
curl -X POST http://localhost:8080/api/bus \
  -H "Content-Type: application/json" \
  -d '{"matricule":"TUN-123","description":"Bus ligne 1","trajetId":1}'

# 2. Envoyer une position
curl -X POST http://localhost:8080/api/location \
  -H "Content-Type: application/json" \
  -d '{"busId":1,"latitude":36.8065,"longitude":10.1815,"timestamp":1700000000000}'

# 3. Récupérer la dernière position
curl http://localhost:8080/api/location/1
```

### Test WebSocket (navigateur)

```html
<!DOCTYPE html>
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2/lib/stomp.min.js"></script>
</head>
<body>
    <div id="positions"></div>
    
    <script>
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);
        
        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/topic/bus-location', function(message) {
                const location = JSON.parse(message.body);
                document.getElementById('positions').innerHTML += 
                    `<p>Bus ${location.busId}: ${location.latitude}, ${location.longitude}</p>`;
            });
        });
    </script>
</body>
</html>
```

## 📊 Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Logs
```bash
# Docker
docker logs geolocalisation-service -f

# Docker Compose
docker-compose logs -f geolocalisation-service
```

## 🔧 Développement

### Structure du projet
```
geolocalisation-service/
├── src/
│   └── main/
│       └── java/com/buapp/geolocalisation_service/
│           ├── GeolocalisationServiceApplication.java
│           ├── config/
│           │   ├── RedisConfig.java
│           │   └── WebSocketConfig.java
│           ├── controllers/
│           │   ├── BusController.java
│           │   └── BusLocationController.java
│           ├── dtos/
│           │   └── LocationDTO.java
│           ├── models/
│           │   ├── Bus.java
│           │   └── Location.java
│           ├── repositories/
│           │   ├── BusRepository.java
│           │   └── LocationRepository.java
│           └── services/
│               ├── BusService.java
│               └── BusLocationService.java
├── Dockerfile
└── pom.xml
```

### Build local
```bash
./mvnw clean package
java -jar target/geolocalisation-service-0.0.1-SNAPSHOT.jar
```

### Build Docker
```bash
docker build -t geolocalisation-service .
docker run -p 8080:8080 geolocalisation-service
```

## 🚨 Troubleshooting

### Le service ne démarre pas
- Vérifier que PostgreSQL est accessible
- Vérifier que Redis est accessible
- Consulter les logs: `docker-compose logs geolocalisation-service`

### Pas de diffusion WebSocket
- Vérifier la configuration CORS
- Vérifier que le client se connecte au bon endpoint
- Ouvrir la console développeur pour voir les erreurs

### Positions non enregistrées
- Vérifier que le bus existe dans la table `bus`
- Vérifier les logs pour les erreurs de validation

## 📝 Notes

- Les positions sont sauvegardées de manière asynchrone
- Redis n'a pas de TTL sur les positions (cache permanent)
- WebSocket supporte plusieurs clients connectés simultanément
- Hibernate crée automatiquement les tables (ddl-auto=update)

## 🔗 Intégration

Pour intégrer ce service dans votre frontend:

1. **Connexion WebSocket** pour le tracking temps réel
2. **API REST** pour récupérer les positions à la demande
3. **Librairie de carte** (Leaflet, Google Maps) pour affichage visuel

## 📄 License

Partie du projet Bus Transport App
