FROM amazoncorretto:8-alpine3.14

RUN mkdir src

WORKDIR /src

COPY build/libs/bootcamp-java-mysql-project-1.0-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "bootcamp-java-mysql-project-1.0-SNAPSHOT.jar"]