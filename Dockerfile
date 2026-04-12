# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project and skip tests (since I already ran them)
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight Docker image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the compiled .jar file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port our Spring Boot app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]