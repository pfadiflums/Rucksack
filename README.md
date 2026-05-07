# Rucksack

Backend API für die Website der **Pfadi St. Justus Flums**.

Erreichbar unter: [api.pfadiflums.ch](https://api.pfadiflums.ch)

## Übersicht

Rucksack ist ein REST-Backend auf Basis von Spring Boot, das nach der hexagonalen Architektur (Ports & Adapters) aufgebaut ist. Es dient als Datenschnittstelle für die Frontend-Anwendung der Pfadi St. Justus Flums.

## Architektur

Das Projekt ist in vier Module aufgeteilt:

| Modul | Beschreibung |
|---|---|
| `domain` | Domänenmodell und Ports (Interfaces) |
| `application` | Use-Cases und Anwendungslogik |
| `infrastructure` | Datenbankanbindung (JPA), externe Adapter |
| `api` | REST-Controller, OpenAPI-Dokumentation |

## Voraussetzungen

- JDK 21
- Docker (für lokale Datenbankentwicklung)

## Lokale Entwicklung

1. `example.env` als `local.env` kopieren und Werte anpassen:

```bash
cp example.env local.env
```

2. Anwendung starten – Docker Compose wird automatisch mitgestartet:

```bash
./gradlew :api:bootRun --args='--spring.profiles.active=dev'
```

Die API ist dann unter `http://localhost:8080/api/v1` erreichbar.  
Swagger UI: `http://localhost:8080/api/v1/docs/swagger`

## Umgebungsvariablen

| Variable | Beschreibung |
|---|---|
| `DB_HOST` | Datenbankhost |
| `DB_PORT` | Datenbankport (Standard: 5432) |
| `DB_NAME` | Datenbankname |
| `DB_USERNAME` | Datenbankbenutzer |
| `DB_PASSWORD` | Datenbankpasswort |

## Build

```bash
./gradlew build
```

## Gruppe

Pfadi St. Justus Flums – [pfadiflums.ch](https://pfadiflums.ch)
