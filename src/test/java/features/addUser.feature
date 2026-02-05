Feature: Add a New User

  Rule: New User to be added successfully

    Scenario: Add a new user with valid details
      Given Add a new user payload
      When User calls "AddUser" with "POST" http request
      Then the API call is successful with status code 200

