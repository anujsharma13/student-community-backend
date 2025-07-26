# Use a base OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar (update the jar name)
COPY target/student-community-backend-1.0-SNAPSHOT.jar app.jar

# Run the jar
CMD ["java", "-jar", "app.jar"]
