# Setup

This project uses Java 11 and Postgres 14.2

It should be compatible with newer versions of both, as well as slightly older Postgres versions

The Java we recommend is Amazon Corretto

We recommend Docker for the postgres install, the following command can be used

> docker run --name pg-14-2 -p 5432:5432 -e POSTGRES_PASSWORD=s3cret -d postgres:14.2

Make sure to create a new postgres database

> create database products_crud;

Then change the spring.datasource in 

> src/main/resources/application.properties 

to match your current setup

## Flyway

This project uses the flyway database schema migration tool

The first time you run the project, your database should be migrated automatically, using

> src/main/resources/db/migration

sql files

You can check the state of migration using the special database table

> flyway_schema_history

The console should also say something like

> Migrating schema "public" to version "1 - create-products-table"

> Successfully applied 1 migration to schema "public", now at version v1 (execution time 00:00.099s)

## Tests

You can make sure the project works correctly by running the tests. The main test class is ProductTest. Another test class is ExchangeRateTest

# Changes

## MockMvc and Mockito

We replaced Wiremock calls with Mockito mocks (Mockito is included with Spring Boot)
