# ExamByte Dokumentation

## 1. Einführung und Ziele

ExamByte ist ein Testsystem für die Klausurzulassung im Programmierpraktikum. Das System:
- Verwaltet Tests (Multiple-Choice und Freitext)
- Ermöglicht Korrekturen durch Korrektor:innen (TODO)
- Zeigt Ergebnisse und Zulassungsstatus (TODO)

## 2. Randbedingungen

**Technisch:**
- Framework: Spring Boot
- Build-System: Gradle
- Datenbank: PostgreSQL
- Authentifizierung: OAuth via GitHub
- Frontend: Thymeleaf Templates
- Java Version: 21

**Organisatorisch:**
- Rollen werden aus application.yaml geladen
- Kein dynamisches Rollen-Management nötig

## 3. Kontextabgrenzung

```
+---------------+     +-----------+     +------------------+
|    GitHub     |     | ExamByte  |     |   PostgreSQL    |
|  OAuth Server |<--->|  System   |<--->|    Database     |
+---------------+     +-----------+     +------------------+
                           ^
                           |
                     +------------+
                     |  Browser   |
                     +------------+
```

## 4. Lösungsstrategie

### 4.1 Aufbau der Anwendung
- Onion-Architektur
- Controller für Web-Interface
- Services für Geschäftslogik
- Repositories für Datenbankzugriff

### 4.2 Sicherheitskonzept
- OAuth2 Authentifizierung über GitHub
- Rollenbasierte Autorisierung (STUDENT, KORREKTOR, ORGANIZER)
- CSRF-Schutz durch Spring Security
- XSS-Schutz durch Thymeleaf Escaping

## 5. Bausteinsicht

### Domain-Objekte
- `Test`: Testkonfiguration mit Zeitangaben
- `Frage`: Abstraktion für Testfragen (MC/Freitext)
- `Antwort`: Studentische Antworten
- `Korrektur`: Bewertungen
- `User`: Benutzermodell

### Controller
- `OrganisatorController`: Test-/Fragenverwaltung
- `LandingPageController`: Login und Startseite

### Services
- `CustomOAuth2UserService`: OAuth und Rollenverwaltung

## 6. Laufzeitsicht

Wichtige Abläufe:
1. **Login**: GitHub OAuth -> Rollenzuweisung -> Weiterleitung
2. **Test-Erstellung**: Zeitpunkte festlegen -> Fragen erstellen -> MC-Items hinzufügen

## 7. Verteilungssicht

Einfache Deployment-Struktur:
- Ein Spring Boot Container
- PostgreSQL Datenbank
- Flyway für Datenbank-Migration

## 8. Querschnittliche Konzepte

### 8.1 Entwicklungskonzepte
- Testgetriebene Entwicklung
- Continuous Integration
- Code-Qualitätssicherung durch:
  - Checkstyle
  - SpotBugs
  - Spotless

### 8.2 Sicherheitskonzepte
- Spring Security Integration
- CSRF-Schutz
- XSS-Prävention

## 9. Architekturentscheidungen

- GitHub OAuth für einfache Authentifizierung
- Thymeleaf für serverseitiges Rendering
- Flyway für versionierte DB-Schemas

## 10. Qualitätsanforderungen

- Korrekte Autorisierung
- Sichere Datenspeicherung
- Benutzerfreundlichkeit

## 11. Risiken

- Abhängigkeit von GitHub
- Datenbankschema-Änderungen (siehe Flyway Migrationen V1-V3)
- Testabdeckung zu gering (nur für die Landingpage und Organisatoren Controller)

## 12. Glossar

- MC: Multiple Choice
- OAuth: Open Authorization Protocol
- CSRF: Cross-Site Request Forgery
- XSS: Cross-Site Scripting
