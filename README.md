# Doctor's Office API
an API for a Doctor's Office Management System. The system should
allow doctors and administrative staff to manage appointments, patient records, prescriptions, and
medical histories.# Prerequisites

In order to run the application, you need to have:
1. JDK 17.0.6 (https://www.oracle.com/java/technologies/downloads/)
2. Maven 3.6.3 (https://maven.apache.org/download.cgi).
3. Git (https://git-scm.com/downloads)
   You also need to configure your user and system path variables for both java and maven. You also need to have git installed in order to clone the project .

### Run it locally
1. Open terminal and clone the repo:
```shell
git clone https://github.com/tobiiii/dom.git
```
2. Make sure you are in project directory  in order to build and package the application with the following Cmd Commande:
```shell
mvn clean install
```
3. Go to /target inside TicTacToe and run the following command to lunch the application:
```shell
java -jar dom-0.0.1-SNAPSHOT.jar
```

## Data Base

This application has postgreSQL 14.7 database,

You can access it by browsing : jdbc:postgresql://localhost:5432/dom

Password = postgres

### Docker
To create an image from our Dockerfile, we have to run ‘docker build':
```shell
$> docker build --tag=dom:0.0.1-SNAPSHOT .
```

Finally, we're able to run the container from our image:
```shell
$> docker run -p8887:8888 dom:0.0.1-SNAPSHOT
```


## API Documentation

The documentation of the API is made by OpenAPI 3.0 , it shows all the Endpoints of the application

You can access after running the application it in to http://localhost:8081/swagger-ui/

##  User Guide

once you run the project spring jpa will automatically create the tables from the entities and then the MyApplicationListener class will run the setup which populate the database from csv files with the minimum data you need to user the api.


###  Use the api :

please get into : http://localhost:8081/swagger-ui/

then follow these instructions :

1. Click on [POST] /api/auth/authentification
2. Click on "Try it out" Button
3. You need and email and a password to authentifie if you are not the super-admin you need to asq the admin to create a user for you.
```shell
{
 "password": "Admin@23",
 "email": "superadmin@test.com"
}
```
4. There are 3 type of users (DOCTOR, PATIENT, STAFF) each one have different profile with different privileges
5. Once you log in you got an access token that you have to send in any request you made.

###  Examples of requests :
create a doctor:

request:

[POST]
/api/doctor/add

```shell
{
    "firstName": "test",
    "lastName": "test",
    "emailAddress": "test@test11.dz",
    "specialization": "pediatrician"
 }
 ```


Update an appointment:

request:

[PUT]
/api/appointment/update/84

```shell
{
    "doctor": 80,
    "patient": 83,
    "dateAndTime": "2023-04-19",
    "reason": "illness"
 }
  ```




