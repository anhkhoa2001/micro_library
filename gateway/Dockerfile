FROM openjdk:11-jdk-slim

COPY /gateway/config config
COPY /gateway/target/gateway-5.1.jar gateway.jar
CMD ["java", "-jar", "gateway.jar"]