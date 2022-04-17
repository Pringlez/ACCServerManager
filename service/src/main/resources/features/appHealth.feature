Feature: Check application health
    Scenario: Client makes call to GET /actuator/health
        Given the health service url is "/actuator/health"
        When the client calls /health and response status code of 200
