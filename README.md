# Restaurants GeoSpatial

Simple Reactive HTTP API covers the methods presents on this article https://docs.mongodb.com/manual/tutorial/geospatial-tutorial/

## Technologies
 
- Kotlin
- Functional programing
- Reactive programing
- Spring 5
- Spring-boot 2
- Coroutines
- Geospatial Queries
- Mongo
- Junit5
- Mockk
- Docker
- Docker Compose

## Getting Started

### Prerequisites

What things you need to install the software and how to install them

```
Java8, Maven, Docker
```
### Running

To run this service:

```shell
$ mvn spring-boot:run
```

To build these services:

```shell
$ sudo docker-compose build & sudo docker-compose up -d OR sudo docker-compose up --build
```


## Sample requests

Get all neighborhoods
```shell
$ curl --header "Content-Type: application/stream+json" http://localhost:8080/reactivemap/v1/neighborhoods
```
Get neighborhood by one specific point
```shell
$ curl --header "Content-Type: application/stream+json" http://localhost:8080/reactivemap/v1/neighborhoods/-73.93414657/40.82302903
```
Get all restaurants by neighborhood
```shell
$ curl --header "Content-Type: application/stream+json" http://localhost:8080/reactivemap/v1/neighborhoods/-73.93414657/40.82302903/restaurants
```
Get all restaurants
```shell
$ curl --header "Content-Type: application/stream+json" http://localhost:8080/reactivemap/v1/restaurants
```
Get all nearby restaurants by radius
```shell
$ curl --header "Content-Type: application/stream+json" "http://localhost:8080/reactivemap/v1/restaurants?longitude=-73.9440286&latitude=40.7006576&radius=0.00000000000000000001"
```
Get all nearby restaurants by distance
```shell
$ curl --header "Content-Type: application/stream+json" "http://localhost:8080/reactivemap/v1/restaurants?longitude=-73.94195&latitude=40.823392&distance=21.0"
```

## Project Structure
- [restaurantsgeospatial]
	 - [main/kotlin]
	    - [/controller] : Controllers with API REST mapping
	    - [/service] : Services for the business logic.
	    - [/repository] : Respository from Data Classes.
	    - [/domain] : Data Classes.
	    - [/exception] : RequestExceptions.
	    - [/deserializer] : Desearilizer of GeoJsons

	 - [test/kotlin]
	    - [/integraion] : Integration tests.
	    - [/service] : Unit tests for services.
	    - [/repository] : Unit tests for repositories.

## Running the tests

mvn test

## Authors

* **Hans de Melo** - [Linkedin](http://linkedin.com/in/hanscamelo/)


## License

This project is licensed under the MIT License