Feature: Check application health
    Scenario: client makes call to GET /health
        Given the health service url is "/actuator/health"
        When the client calls /health and response status code of 200
