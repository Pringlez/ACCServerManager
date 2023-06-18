Feature: Verify events API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured events, validate response structure and elements
    Given the endpoint is "/api/v1/events/<instanceId>"
    And a basic auth header "Authorization" with a value "admin:admin" is included in the request
    And a query parameter "eventId" with a value "<eventId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "health"
    And the response time should be less then or equal to 5 seconds
    And the response results object should contain 5 elements
    And the response contains "success" element with value of "<success>"
    Examples:
      | instanceId                           | eventId                              | schema        | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | fd2e44af-e420-4e2c-ac54-232fd61e3748 | getEvent.json | 200    |
