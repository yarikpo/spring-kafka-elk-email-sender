FROM openjdk:17-alpine

RUN apk update && apk upgrade

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ./app.jar
COPY .env ./.env
# COPY . /app

# RUN ./mvnw clean package

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
# ENTRYPOINT ["java", "-jar", "/app/target/email-sender-0.0.1-SNAPSHOT.jar"]