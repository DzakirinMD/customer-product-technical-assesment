FROM eclipse-temurin:17-jdk-focal
LABEL "base.image"="eclipse-temurin:17-jdk-focal"

ENV ENVIRONMENT default
ARG JAR_FILE=target/order-management-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 10001

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
