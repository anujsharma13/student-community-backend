# ==============================
# Stage 1: Build with Maven
# ==============================
FROM maven:3.9.6-eclipse-temurin-17 as builder

# Set working directory
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Package the application (clean + install)
RUN mvn clean install -DskipTests

# ==============================
# Stage 2: Run with JDK only
# ==============================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=builder /app/target/student-community-backend-1.0-SNAPSHOT.jar app.jar

# Start the application
CMD ["java", "-jar", "app.jar"]
