"# AdServer" 

Ad Server project is a standalone Spring Boot App written in Java
leveraging Java 8, Spring, and Spring Boot.

Application:
com.jeffdonovan.adserver.AdserverApplication.java

Contains spring boot configuration and main routine. To run appication run this class
as a SpringBoot application.

If I had more time....
I would improve code comments.
Write unit and integration tests for AdService.

Domain:
com.jeffdonovan.adserver.Ad.java

Sample JSON representation:
{
  "partnerId": 10,
  "duration": 100,
  "expiration": 2000,
  "adContent": "Content String Partner Id 10"
}

Controller:
AdController.java

Exposes three end points.
                      
GET localhost:8080/ad (No URL parameters required)                   Get all Ad resources currently in DB
SampleResponse:
Http Status: 200 OK
[
  {
    "partnerId": "10",
    "duration": 1,
    "expiration": 1494184992,
    "adContent": "Content String Partner Id 8"
  },
  {
    "partnerId": "11",
    "duration": 1,
    "expiration": 1494188210,
    "adContent": "Content String Partner Id 8"
  }
]

POST localhost:8080/ad (No URL parameters required)                   Save an Ad resource                   
Sample Request:        
{
  "partnerId": "11",
  "duration": 1,
  "expiration": 1494188210,
  "adContent": "Content String Partner Id 8"
}
Sample Response:
CREATED
Http Status: 201 Created
{
  "partnerId": "11",
  "duration": 1,
  "expiration": 1494188210,
  "adContent": "Content String Partner Id 8"
}

GET localhost:8080/ad/{partnerId}  (No URL parameters required)    Get an Ad resource by partner Id   
Sample Response:
CREATED
Http Status: 200 OK
{
  "partnerId": "11",
  "duration": 1,
  "expiration": 1494188210,
  "adContent": "Content String Partner Id 8"
}      