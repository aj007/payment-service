FROM openjdk:21
WORKDIR /app
COPY ./target/payment-service-1.0.0-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "payment-service-1.0.0-SNAPSHOT.jar"]