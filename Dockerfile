FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/order-system-0.0.1-SNAPSHOT.jar /app/order-system-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java","-jar","/app/order-system-0.0.1-SNAPSHOT.jar"]