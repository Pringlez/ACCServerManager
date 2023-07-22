Feature: Verify event rules API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured event rules, validate response structure and elements
    Given the endpoint is "/api/v1/eventrules/<instanceId>"
    And a basic auth header "Authorization" with a value "user-1:vxUdzhqrwt8eqQS7yszq" is included in the request and base64 encoded
    And a query parameter "eventRulesId" with a value "<eventRulesId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "event-rules"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 36 elements
    And the response contains "id" element with value of "<eventRulesId>"
    Examples:
      | instanceId                           | eventRulesId                         | schema             | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | 0be87ff1-ce8a-4f9a-861f-8fe48bc57057 | getEventRules.json | 200    |
