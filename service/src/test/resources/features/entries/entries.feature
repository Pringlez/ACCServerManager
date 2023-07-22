Feature: Verify entries API functionality & structure

  @Api
  Scenario Outline: Retrieve instance's configured entries, validate response structure and elements
    Given the endpoint is "/api/v1/entries/<instanceId>"
    And a basic auth header "Authorization" with a value "user-1:vxUdzhqrwt8eqQS7yszq" is included in the request and base64 encoded
    And a query parameter "entriesId" with a value "<entriesId>" is included in the request
    When a GET request is executed we should receive <status> status code
    Then the response structure should match "<schema>" file in directory "entries"
    And the response time should be less then or equal to 1 seconds
    And the response results object should contain 7 elements
    And the response contains "id" element with value of "<entriesId>"
    Examples:
      | instanceId                           | entriesId                            | schema          | status |
      | ae85423a-b502-4833-bcc2-a424d3f8281e | 11c8f89b-3745-4cc5-905b-1dc0c1464281 | getEntries.json | 200    |
