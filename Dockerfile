FROM maven:3.8.5-openjdk-11 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY src/Server/target/Server-1.0.jar /app
EXPOSE 333
CMD ["java", "-jar", "Server-1.0.jar"]