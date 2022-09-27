[![Build Status](https://app.travis-ci.com/lauur/juniorjavaoffers.svg?branch=main)](https://app.travis-ci.com/lauur/juniorjavaoffers)

# Junior Java Offers Application

Web application for job search for Junior Java Developers. The main function of the application is to download current
job offers from websites.

App is secured with Spring Security. There are two user roles: USER and ADMIN.
ADMIN role has additional permissions to add and delete offers.

## Technologies

- Java 8
- SpringBoot
- Spring Security
- MongoDB
- Redis
- JUnit 5
- AssertJ
- Docker
- **frontend:** React

## Rest Methods

![](https://user-images.githubusercontent.com/92169813/192292692-bc9be961-b495-4c7e-a6b5-f1b299530346.png)

## Setup

To run this project:

```bash
  cd mongo
  docker compose up
```

To start application, in the project directory:

```bash
  ./mvnw spring-boot:run
```

To start frontend:

```bash
  cd src/frontend
  npm install
  npm start
```

- App runs by default on: http://localhost:8080
- Frontend runs by default on: http://localhost:3000
