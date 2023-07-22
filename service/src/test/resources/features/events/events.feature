Feature: Verify events API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured events, validate response structure and elements
    Given the endpoint is "/api/v1/events/<instanceId>"
    And a basic auth header "Authorization" with a value "user-1:vxUdzhqrwt8eqQS7yszq" is included in the request and base64 encoded
    And a query parameter "eventId" with a value "<eventId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "events"
    And the response time should be less then or equal to 5 seconds
    And the response results object should contain 5 elements
    And the response contains "id" element with value of "<eventId>"
    Examples:
      | instanceId                           | eventId                              | schema        | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | 91098aa8-3f7d-4e98-bcbd-82d2398de655 | getEvent.json | 200    |
