Feature: Verify event rules API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured event rules, validate response structure and elements
    Given the endpoint is "/eventrules/<instanceId>"
    And a query parameter "eventRulesId" with a value "<eventRulesId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "vehicles"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 36 elements
    And the response contains "success" element with value of "<success>"
    Examples:
      | instanceId                           | eventRulesId                         | schema          | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | 2d050cfd-1337-4a1b-b86e-63f2b27f1c38 | getVehicle.json | 200    |
