FROM openjdk:11-jdk-slim

COPY /user/target/user-5.1.jar user.jar
CMD ["java", "-jar", "user.jar"]

#COPY /gateway/config config
#COPY /gateway/target/gateway-5.1.jar app.jar
#CMD ["java", "-jar", "app.jar"]