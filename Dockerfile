# Use OpenJDK 21 as base image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=docker

# Copy the JAR file into the container (make sure to match the exact filename)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


