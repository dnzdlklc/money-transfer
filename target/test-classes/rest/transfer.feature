Feature: Transfer REST API

  Scenario: Transfer between two clients accounts
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 10000.00       |
      | Jane      | Doe      | 20000.00       |
    When client requests POST /transfer with following body
      | fromAccount | <JohnDoeAccountId> |
      | toAccount   | <JaneDoeAccountId> |
      | amount      | 5000.00            |
    Then response status is 200
    And response includes the following transfer status
      | transferStatus | OK |
    And balance of accounts are following
      | accountId        | balance  |
      | <JohnDoeAccount> | 5000.00  |
      | <JaneDoeAccount> | 25000.00 |

  Scenario: Insufficient funds to perform transfer
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 800.00         |
      | Jane      | Doe      | 200.00         |
    When client requests POST /transfer with following body
      | fromAccount | <JohnDoeAccountId> |
      | toAccount   | <JaneDoeAccountId> |
      | amount      | 1000.00            |
    Then response status is 400
    And response includes the following message
      | statusCode | 400                                                                     |
      | message    | Insufficient funds to perform withdraw from account: <JohnDoeAccountId> |

  Scenario: Transfer to non existing account
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 800.00         |
    When client requests POST /transfer with following body
      | fromAccount | <JohnDoeAccountId>     |
      | toAccount   | <NonExistingAccountId> |
      | amount      | 100.00                 |
    Then response status is 400
    And response includes the following message
      | statusCode | 400                                                                |
      | message    | Account with id <NonExistingAccountId> (toAccount) cannot be found |

  Scenario: Transfer from non existing account
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 800.00         |
    When client requests POST /transfer with following body
      | fromAccount | <NonExistingAccountId> |
      | toAccount   | <JohnDoeAccountId>     |
      | amount      | 100.00                 |
    Then response status is 400
    And response includes the following message
      | statusCode | 400                                                                  |
      | message    | Account with id <NonExistingAccountId> (fromAccount) cannot be found |

  Scenario: Transfer of negative amount
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 800.00         |
      | Jane      | Doe      | 200.00         |
    When client requests POST /transfer with following body
      | fromAccount | <JohnDoeAccountId> |
      | toAccount   | <JaneDoeAccountId> |
      | amount      | -100.00            |
    Then response status is 400
    And response includes the following message
      | statusCode | 400                                  |
      | message    | Amount value has to be higher than 0 |