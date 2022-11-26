# onebox-ecommerce-backend-app
Simple ecommerce backend app demo for Onebox

## Description

This repository contains the implementation of an API exposing endpoints that enable the use of a microservice intented to run as a backend for a simple ecommerce app. The implementation uses Guava Cache from the Google Guava core libraries for Java to provide an in-memory data storage with a set TTL.

## Technology required 

This code uses Java 17 and Gradle 7.6

## Running the code

First, you can clone the repository using

```
git clone https://github.com/ErnestCV/onebox-ecommerce-backend-app.git
```

Then, you can build the project with 

```
./gradlew build
```

And run it using

```
./gradlew bootRun
```

## Docker

You can also build a docker image once you've built the project by using

```
docker build -t onebox:ecommerceapp-v1.0-RELEASE .   
```

And run it in a container with

```
docker run -d --name ecommerceapp -p 8751:8751 onebox:ecommerceapp-v1.0-RELEASE   
```

## Postman

The repository includes a folder with a set of Postman requests to manually test or use the API.

## Documentation

Once the application is running, you may see the documentation by accessing http://localhost:8751/swagger-ui/index.html, which lists all the available endpoints.
