Feature: Verify instances API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's full configuration and status, validate response structure and elements
    Given the endpoint is "/instances/<instanceId>"
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "instances"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 13 elements
    And the response contains "success" element with value of "<success>"
    Examples:
      | instanceId                           | schema            | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | getInstances.json | 200    |
