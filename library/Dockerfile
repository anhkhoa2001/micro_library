FROM openjdk:11-jdk-slim

COPY /library/target/library-5.1.jar library.jar
CMD ["java", "-jar", "library.jar"]

#COPY /gateway/config config
#COPY /gateway/target/gateway-5.1.jar app.jar
#CMD ["java", "-jar", "app.jar"]