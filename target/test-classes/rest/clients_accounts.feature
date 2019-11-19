Feature: Clients / Accounts REST API

  Scenario: Retrieving all clients
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    When client requests GET /clients
    Then response status is 200
    And response contains 2 clients
    And response includes the following clients
      | order | firstName | lastName |
      | 0     | John      | Doe      |
      | 1     | Jane      | Doe      |

  Scenario: Retrieving single client
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    When client requests GET /clients/<JohnDoeId>
    Then response status is 200
    And response includes the following client
      | firstName | John |
      | lastName  | Doe  |

  Scenario: Retrieving non existing client
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    When client requests GET /clients/<NonExistingId>
    Then response status is 404
    And response includes the following message
      | statusCode | 404                                             |
      | message    | Client with id: <NonExistingId> cannot be found |

  Scenario: Retrieving all accounts for given client
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 0.00           |
      | John      | Doe      | 10000.00       |
    When client requests GET /clients/<JohnDoeId>/accounts
    Then response status is 200
    And response includes the following accounts
      | order | balance  |
      | 0     | 0.00     |
      | 1     | 10000.00 |

  Scenario: Retrieving single account for given client
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 10000.00       |
    When client requests GET /clients/<JohnDoeId>/accounts/<JohnDoeAccountId>
    Then response status is 200
    And response includes the following account
      | balance | 10000.00 |

  Scenario: Retrieving non existing account for given client
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
    When client requests GET /clients/<JohnDoeId>/accounts/<NonExistingAccountId>
    Then response status is 404
    And response includes the following message
      | statusCode | 404                                                     |
      | message    | Account with id: <NonExistingAccountId> cannot be found |