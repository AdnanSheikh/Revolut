
# Fund Transfer Rest API

Java RESTful API for transfer money between users account in multi-currencies. Currency conversion applied before transaction in different currency accounts.

<h1>Problem Statement</h1>

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between internal users/accounts.

Explicit requirements:

keep it simple and to the point (e.g. no need to implement any authentication, assume the APi is invoked by another internal system/service)
use whatever frameworks/libraries you like (except Spring, sorry!) but don't forget about the requirement #1
the datastore should run in-memory for the sake of this test
the final result should be executable as a standalone Java program
demonstrate with tests that the API works as expected
Implicit requirements:

the code produced by you is expected to be of good quality
there are no detailed requirements, use common sense
Please put your work on github or bitbucket.

<h2>Technology Stack</h2>
 
 - Java 8
 - H2/ SQLLite in-memory database (Configurable) 
 - Jetty container
 - JAX-RS API
 - Apache HTTP Client (for tests)
 - Jackson 
 - Maven 
 - SLF4J 
 
 <h2>Project building</h2>
 
    mvn clean install exec:java
    
 <h2> Sample Json Request </h2>
 
 <h4> Customer creation</h4>
 
    Request: http://localhost:8080/customer/create
    {
        "firstName": "Adnan",
        "lastName": "Ahmad",
        "personalIdentityNo": "ZET2324S"
    }
    
    
    Response
    {
        "message": "Customer created...",
        "customerId": 1,
        "customer": {
            "firstName": "Adnan",
            "lastName": "Ahmad",
            "personalIdentityNo": "ZET2324S"
         }
     }
    
   
  <h4> Account creation </h4>
  
    Request: http://localhost:8080/account/create
    {
       "customerId": 12,
       "currencyCode": "CHF",
       "amount": 99.38
    }
    
    Response
    {
        "message": "Account created ...",
        "accountId": 121,
        "account": {
            "customerId": 12,
            "currencyCode": "CHF",
            "amount": 99.38
         }
    }
    
    
   <h4> Transaction </h4>
  
    Request:  http://localhost:8080/transaction/execute
    {
      "fromAccountId": 123,
      "toAccountId": 243,
      "amount": 10.38
    }
    
    Response
    {
       "message": "Transaction successfully completed...",
       "transactionId": 142,
       "transaction": {
           "fromAccountId": 123,
           "toAccountId": 243,
           "amount": 10.38
        }
    }
