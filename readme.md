# AMPx Interview

## Hello

Here is my solution of the given requirements, see below *Requirements* section.    
I DID not implement moving average feature YET - will ad it later today.

## What I have simplified / ignored

* General coding standard
    * The package structure is rather flat - no need yet for grouping for domain/architectrue purposes
    * I've also ignored most of the javadoc, only added to the most important class, ideally it should be on every
      public interface.
    * I've failing fast - if the data does not exists or are invalid, I am throwing an exception. This could be solved
      differently, but in this demo its adequate. I think.
    * Inserted some dummy data to database directly upon application startup (but at least hide it behind dev profile).
    * I did not spend time on creating Dockerfile, nor docker-compose. No need for that, as everything need for
      deployment is already bundled inside spring boot fatjar
* Testing
    * In general, I've created just few samples of tests to show you how I am used to writing them, and DID NOT try to
      cover everything. In real life, I test for edge cases (nulls, invalid values, range values), positive and negative
      scenario
    * when testing controllers, I defined request jsons in plain string. If there will be more of those tests, I would
      think of moving them to files for better readability and maintenance (even reusability - sample json request could
      be used by other tools during automation, not just unit tests)
    * Spring context in testing classes could have been defined better - no time to dig into that :/
* Data modelling
    * To follow the requirements (have input object with param user, device) but also store in database I am mapping the
      values between DTO and domain object
    * I am not defining any more data structure other that already mentioned in the requirements. So no username, device
      description and anything like that.
    * For database entities, I make them as simple as possible. e.g. chosen Long for id, not setting up any sequence for
      generating values, not using any data migration tool like Liquibase.
    * In real life, I would use builder for creating domain objects, making the final with no set methods/constructor
      available to outside
    * Error POJO used for HTTp error responses would contain more details about the issue at hand, not only the message
    * I've generated equals, toString and hash code to all POJOS, just because I dont want to solve any issues whenever
      I will sort the items or work with them in collection. In real life, I would invest more time to verify libraries
      like @Lombok could be utilized

## My approach

1. I've created Spring project scaffold through Spring Initializr
1. Created application.yml, configured some basics like h2-console, auto DDL updates, sql logging etc.
1. Design database entities in the most simplistic way possible
1. Added entities constraints and hibernate validations. These are validations hard stop, but expect to add something on
   the DTO and service level as well.
1. Created DatapointController, autowired service interface, create mvnTest for that
1. Implemented DatapointService and its tests for storing the datapoints
1. Inserted some dummy data during application startup -> verify the POST endpoint works as expected. Accepts data,
   validates them, saves them or reporting exceptions correctly as HTTP errors
1. __Now that I know the majority (database, error handling, mapping, testing) of the code is done, I can focus on the
   business logic, copy&pasting the already created concepts__
1. Created device delete endpoint, improved error handling, added simple API tests
1. Create user delete endpoint, mostly copy&paste of device endpoint. Everything important is already working, adding
   new stuff is easy
1. Created statistics service for averages, created controller, wire together
1. Added swagger api documentation
1. Once again test everything, finalize readme.md, push to github, sent and forgot :-)

## How to run it

If you have git, and java (11), then simply:

1. Clone the repo `git clone git@github.com:rudypokorny/ampx.git`
1. `./mvnw install` to build it
1. `./mvnw spring-boot:run` to run it using your local java installation
1. Start with dev profile to generate some data,
   e.i. `./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev`

Application will be, by default running on port 8080 in http, so visit
e.g. http://localhost:8080/statistics/devices/1/avg to view something useful Swagger API documentation is available
at http://localhost:8080/swagger-ui

## Requirements

Write a rest server to track a data time series. Implement endpoints described below with appropriate response statuses.
The server should keep the data only in memory, no persistency is necessary.

- POST should accept json object with keys described in brackets (all mandatory fields)
- POST /datapoints [timestamp;value;device;user] - add new datapoint to time series. Tuple {timestamp, device, user} is
  unique. Adding the same point should cause bad request response and not change the data.
- DELETE /devices/{device}/datapoints - delete all device datapoints
- DELETE /users/{user}/datapoints - delete all user datapoints
- GET /statistics/devices/{device}/avg - return list of 15 minutes averages of time serie from first datapoint to
  current time. Matching device key
- GET /statistics/devices/{device}/moving_avg?window_size={window_size} - return list of moving averages of 15 minutes
  average buckets. I.E. moving averages of result /statistics/devices/{device}/avg
- GET /statistics/users/{user}/avg - return list of 15 minutes averages of time serie from first datapoint to current
  time. Matching user key
- GET /statistics/users/{user}/moving_avg?window_size={window_size} - return list of moving averages of 15 minutes
  average buckets. I.E. moving averages of result /statistics/devices/{device}/avg

Good architecture design and clean code are also part of the assessment. We look forward to your solution and also to
your suggestions on how to improve the rest endpoint architecture




