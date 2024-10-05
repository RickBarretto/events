Feature: Registering a new user
    In order to register new users
    As an Anonymous User
    I want to create a new Account

    Scenario: Registering a new User
        Given An anonymous User 
            And an inexistent registered Account
        When creating a new Account
        Then A new account is created following the User's input

    Scenario: Registering an existent User
        Given An anonymous User 
            And an existent registered Account
        When creating a new Account
        Then A new account can't be created
            And throws UserAlreadyRegistered
    