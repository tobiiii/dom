FROM openjdk:17-alpine
COPY /target/dom-0.0.1-SNAPSHOT.jar  dom-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar", "/dom-0.0.1-SNAPSHOT.jar"]