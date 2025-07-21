FROM openjdk:24

WORKDIR /app

COPY ./target/jobboard-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
