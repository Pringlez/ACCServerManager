Feature: Verify entries API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured entries, validate response structure and elements
    Given the endpoint is "/api/v1/entries/<instanceId>"
    And a basic auth header "Authorization" with a value "admin:admin" is included in the request
    And a query parameter "entriesId" with a value "<entriesId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "entries"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 7 elements
    And the response contains "success" element with value of "<success>"
    Examples:
      | instanceId                           | entriesId                            | schema          | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | b6beac63-6b7c-4d0d-b469-3bc2f0d5781c | getEntries.json | 200    |
