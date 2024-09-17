# Use a base image with JDK
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew /app/
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src
COPY build.gradle.kts /app/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts

# Ensure the Gradle wrapper is executable
RUN chmod +x gradlew

# Build the Spring Boot application
RUN ./gradlew clean build --no-daemon -x test

# Expose the port that your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "build/libs/shop-0.0.1-SNAPSHOT.jar"]
