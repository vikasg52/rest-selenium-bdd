Feature: Restful Booker API Validation
  @smoke
  Scenario Outline: Validation of booking creation with dynamic payload
    Given I set the base URL for the API
    When I send a POST request to "/booking" with the following payload:
      | key                   | type    | value        |
      | firstname             | String  | <firstname>  |
      | lastname              | String  | <lastname>   |
      | totalprice            | Number  | <totalprice> |
      | depositpaid           | Boolean | true         |
      | bookingdates.checkin  | String  | 2025-01-01   |
      | bookingdates.checkout | String  | 2025-01-05   |
      | additionalneeds       | String  | Breakfast    |
    Then the response status code should be 200
    And the response should contain the booking details

    Examples:
      | firstname | lastname | totalprice |
      | John      | Doe      | 100        |
      | Jane      | Smith    | 200        |
      | Alice     | Johnson  | 300        |

#DP.SPLIT.01
  @fields @validation
  Scenario Outline: Check mandatory fields: <param> for booking creation
    Given I set the base URL for the API
    Given order API with params:
      | key                   | type    | value                        |
      | firstname             | String  | John                         |
      | lastname              | String  | Doe                          |
      | totalprice            | Number  | 100                          |
      | depositpaid           | Boolean | true                         |
      | bookingdates.checkin  | String  | 2025-01-01                   |
      | bookingdates.checkout | String  | 2025-01-05                   |
      | additionalneeds       | String  | Breakfast                    |
    When remove <param> from JSON params
    And send POST request to "/booking"
    Then http status must equal 400
    And json body must contain params:
      | name    | INVALID_API_REQUEST |
      | message | <lostParam>         |

    Examples:
      | param                | lostParam                                                  |
      | firstname            | Errors: [fields ['firstname'] may not be empty]             |
      | lastname             | Errors: [fields ['lastname'] may not be empty]              |
      | totalprice           | Errors: [fields ['totalprice'] may not be empty]            |
      | depositpaid          | Errors: [fields ['depositpaid'] may not be empty]           |
      | bookingdates         | Errors: [fields ['bookingdates'] may not be empty]          |
      | bookingdates.checkin | Errors: [fields ['bookingdates.checkin'] may not be empty]  |
      | bookingdates.checkout| Errors: [fields ['bookingdates.checkout'] may not be empty] |
      | additionalneeds      | Errors: [fields ['additionalneeds'] may not be empty]       |

#DP.SPLIT.02
  @validation @negative
  Scenario Outline: Validation negative check of the "totalprice" field with '<validation_fail>'
    Given I set the base URL for the API
    Given order API with params:
      | key                   | type    | value                        |
      | firstname             | String  | John                         |
      | lastname              | String  | Doe                          |
      | totalprice            | Number  | <totalprice>                 |
      | depositpaid           | Boolean | true                         |
      | bookingdates.checkin  | String  | 2025-01-01                   |
      | bookingdates.checkout | String  | 2025-01-05                   |
      | additionalneeds       | String  | Breakfast                    |
    When send POST request to "/booking"
    Then http status must equal 400
    And json body must contain params:
      | name    | INVALID_API_REQUEST |
      | message | <errorMessage>      |

    Examples:
      | validation_fail               | errorMessage                                      | totalprice |
      | empty field                   | Errors: [fields ['totalprice'] may not be empty] |            |
      | negative value                | Errors: [fields ['totalprice'] must be positive] | -100       |
      | zero value                    | Errors: [fields ['totalprice'] must be positive] | 0          |
      | non-numeric value             | Errors: [fields ['totalprice'] must be a number] | abc        |