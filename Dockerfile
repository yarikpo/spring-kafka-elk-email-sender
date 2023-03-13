FROM openjdk:17-alpine

COPY . /java

WORKDIR /java

EXPOSE 8080

RUN ./mvnw clean package

CMD ["java", "-jar", "target/email-sender-0.0.1-SNAPSHOT.jar"]