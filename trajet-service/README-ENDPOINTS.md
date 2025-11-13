# Trajet Service API Endpoints

## Overview
This microservice manages cities (with fixed ticket prices), trajets (routes), stations, ordered station associations (TrajetStation), start times (TrajetTime), and schedule calculations.

Base path (without gateway prefix): `/`
When routed through the API Gateway (configured to strip a leading `/api`), endpoints appear as `/api/{resource}`.

---

## TrajetCity
| Method | Path | Description |
|--------|------|-------------|
| POST | `/trajet-cities` | Create a city with fixed price |
| GET | `/trajet-cities` | List all cities |
| GET | `/trajet-cities/{id}` | Get a city by id |
| PUT | `/trajet-cities/{id}` | Update city name/price |
| DELETE | `/trajet-cities/{id}` | Delete a city |

Request body (create/update):
```json
{ "cityName": "Alger", "priceInDhs": 25.00 }
```
Response body:
```json
{ "id": 1, "cityName": "Alger", "priceInDhs": 25.00 }
```

---

## Station
| Method | Path | Description |
|--------|------|-------------|
| POST | `/stations` | Create station |
| GET | `/stations` | List stations |
| PUT | `/stations/{id}` | Update station name |

Request (create/update):
```json
{ "nom": "Station A" }
```
Response:
```json
{ "id": 5, "nom": "Station A" }
```

---

## Trajet
| Method | Path | Description |
|--------|------|-------------|
| POST | `/trajets` | Create a trajet with ordered stations + city reference; optional startTimes will be saved into `trajet_times` |
| GET | `/trajets` | List all trajets |
| GET | `/trajets/{id}` | Get trajet by id |
| GET | `/trajets/city/{cityId}` | List trajets for a city |
| GET | `/trajets/{id}/schedules` | Compute schedules (arrival times) for each start time |

Create request:
```json
{
  "source": "S1",
  "destination": "S9",
  "cityId": 1,
  "dureeEstimee": "PT45M",  // ISO-8601 Duration
  "stationIds": [10,11,12],
  "stationDurations": [5,10,7],
  "startTimes": ["07:00","07:15"]  // stored into trajet_times table
}
```
Response:
```json
{
  "id": 3,
  "source": "S1",
  "destination": "S9",
  "cityId": 1,
  "dureeEstimee": "PT45M",
  "stationNames": ["Station A","Station B","Station C"],
  "startTimes": ["07:00","07:15"]
}
```
Schedule sample:
```json
[
  {
    "startTime": "07:00",
    "arrivals": [
      {"stationName": "Station A", "arrivalTime": "07:05"},
      {"stationName": "Station B", "arrivalTime": "07:15"},
      {"stationName": "Station C", "arrivalTime": "07:22"}
    ]
  }
]
```

---

## TrajetStation
| Method | Path | Description |
|--------|------|-------------|
| POST | `/trajet-stations` | Create station association for a trajet |
| GET | `/trajet-stations/by-trajet/{trajetId}` | List ordered stations for a trajet |
| GET | `/trajet-stations/{id}` | Get association by id |
| PUT | `/trajet-stations/{id}` | Update order / estimated minutes |
| DELETE | `/trajet-stations/{id}` | Remove station from trajet |

Create request:
```json
{ "trajetId": 3, "stationId": 11, "ordreDansTrajet": 2, "estimatedMinutes": 10 }
```
Response:
```json
{ "id": 21, "trajetId": 3, "stationId": 11, "stationName": "Station B", "ordreDansTrajet": 2, "estimatedMinutes": 10 }
```

---

## TrajetTime
| Method | Path | Description |
|--------|------|-------------|
| POST | `/trajet-times` | Create a start time for a trajet |
| GET | `/trajet-times/by-trajet/{trajetId}` | List start times for a trajet |
| GET | `/trajet-times/{id}` | Get a start time record |
| PUT | `/trajet-times/{id}?startTime=HH:MM` | Update start time |
| DELETE | `/trajet-times/{id}` | Delete start time |
| GET | `/trajet-times/{id}/arrivals` | Compute arrivals for this start time |

Create request:
```json
{ "trajetId": 3, "startTime": "07:00" }
```
Response:
```json
{ "id": 40, "trajetId": 3, "startTime": "07:00" }
```
Arrivals sample:
```json
[
  {"stationName": "Station A", "arrivalTime": "07:05"},
  {"stationName": "Station B", "arrivalTime": "07:15"}
]
```

---

## Error Handling
Errors return HTTP status codes (404 for not found, 400 for validation). Body may be a simple message; a centralized handler can be added later.

## Notes
1. Arrival times derive from cumulative `estimatedMinutes` on `TrajetStation`.
2. If `TrajetTime` records exist for a trajet, schedule endpoint uses them; else falls back to embedded `startTimes` in the trajet.
3. Duration fields use ISO-8601 (e.g. `PT15M`).

## Authentication
Not implemented; all endpoints are open by default. Secure with Spring Security as needed.

---

## OpenAPI Generation
All controllers use `@Tag` and endpoints use `@Operation(summary=...)` enabling OpenAPI documentation when springdoc or similar dependency is added.
