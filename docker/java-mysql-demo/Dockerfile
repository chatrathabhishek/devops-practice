FROM openjdk:8-jdk-alpine

RUN mkdir src

WORKDIR /src

COPY build/libs/bootcamp-java-mysql-project-1.0-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "bootcamp-java-mysql-project-1.0-SNAPSHOT.jar"]
