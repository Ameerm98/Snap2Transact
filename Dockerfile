# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
