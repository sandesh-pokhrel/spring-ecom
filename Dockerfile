FROM openjdk:8-jdk-alpine
EXPOSE 9999
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/kavka.jar
WORKDIR /app
COPY ${JAR_FILE} .
ENTRYPOINT ["java","-jar","kavka.jar"]
