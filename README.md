# Configuration Change Tracker

**Description:**
Spring Boot 3.x app that logs, stores and retrieves configuration changes to domain-specific rules (e.g., credit limits,
approval
policies).

* Provides REST API to add/update/delete, list changes and retrieve by id.
* Provides REST API and health/prometheus endpoints.
* Notifies external systems of critical changes (simulated).
* Stores data in-memory (H2) for simplicity.

**Rationale:**
Centralized tracking of configuration changes improves traceability and enables external systems to react to critical
updates.

---

## Assumptions

* IDs are UUIDs.
* Config Change types: `CREDIT_LIMIT`, `APPROVAL_POLICY`, etc.
* H2 in-memory DB suffices; no persistent storage required.
* Input parameters and request body are validated.
* Critical notifications are simulated; no real external service is called.

---

## Running the App

### Clone & build

* git clone <https://github.com/LukasovecSK/configuration-change-tracker>
* cd configuration-change-tracker
* mvn clean install

### Run the app

mvn spring-boot:run

**H2 Console:**

* URL: `http://localhost:8080/h2-console`
* JDBC: `jdbc:h2:mem:dev`
* User: `admin`, Password: `admin`

---

## Testing

* Unit tests: `*Test.java`
* Integration tests (H2 + MockMvc): `*IT.java`
* Run all tests: `mvn verify`

## Future Improvements

* Replace H2 with a persistent DB.
* Integrate real SNS or messaging service for critical notifications.
