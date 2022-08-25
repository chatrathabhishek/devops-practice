FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY jenkins/java-maven-app/target/java-maven-app-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar java-maven-app-*.jar
