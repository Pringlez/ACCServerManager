Feature: Verify bop API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured bop, validate response structure and elements
    Given the endpoint is "/bop/<instanceId>"
    And a query parameter "bopId" with a value "<bopId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "bop"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 7 elements
    And the response contains "success" element with value of "<success>"
    Examples:
      | instanceId                           | bopId                                | schema      | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | fe3d0405-0cd9-4398-bd10-d0b61922339c | getBop.json | 200    |
