Feature: Verify assists API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured assists, validate response structure and elements
    Given the endpoint is "/api/v1/assistrules/<instanceId>"
    And a basic auth header "Authorization" with a value "admin:admin" is included in the request
    And a query parameter "assistsId" with a value "<assistsId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "assists"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 7 elements
    And the response contains "success" element with value of "<success>"
    Examples:
      | instanceId                           | assistsId                            | schema          | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | e8c04d38-2dbd-46a1-859c-67f44525710c | getAssists.json | 200    |
