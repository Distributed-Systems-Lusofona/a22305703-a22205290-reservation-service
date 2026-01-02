# Reservation Service

This repository contains the Reservation Service, one of the microservices in the Store Project —
a distributed system composed of multiple Spring Boot services that work together to manage an online store.

Each microservice runs in its own container, communicating with others via REST APIs.


Mudamos a nomenclatura dos Request para bater com o que é pedido embora incialmente tenhamos divergido relativamente ao que fazer
cada opração Crud terá o próprio dta transfer object


## Setup Store network

````
docker network create store-network
````