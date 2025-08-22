# Stage 1: Build the Spring Boot app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the jar without tests
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:17-jdk-slim
WORKDIR /app
VOLUME /tmp

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render uses $PORT env variable)
EXPOSE 8090

# Start the application with Render's port
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
