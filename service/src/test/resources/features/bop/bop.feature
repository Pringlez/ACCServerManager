Feature: Verify bop API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured bop, validate response structure and elements
    Given the endpoint is "/api/v1/bop/<instanceId>"
    And a basic auth header "Authorization" with a value "user-1:vxUdzhqrwt8eqQS7yszq" is included in the request and base64 encoded
    And a query parameter "bopId" with a value "<bopId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "bop"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 7 elements
    And the response contains "id" element with value of "<bopId>"
    Examples:
      | instanceId                           | bopId                                | schema      | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | 6f2454e5-0e46-43e5-acda-556e5b71cd86 | getBop.json | 200    |
