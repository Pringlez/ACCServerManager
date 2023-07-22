Feature: Verify assists API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured assists, validate response structure and elements
    Given the endpoint is "/api/v1/assistrules/<instanceId>"
    And a basic auth header "Authorization" with a value "user-1:vxUdzhqrwt8eqQS7yszq" is included in the request and base64 encoded
    And a query parameter "assistsId" with a value "<assistsId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "assists"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 7 elements
    And the response contains "id" element with value of "<assistsId>"
    Examples:
      | instanceId                           | assistsId                            | schema          | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | 045fac40-d440-4e82-805a-e4c98c455fd0 | getAssists.json | 200    |
