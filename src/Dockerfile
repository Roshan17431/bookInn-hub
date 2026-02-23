# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first for better dependency caching
COPY pom.xml .

# Download dependencies (helps with caching)
RUN mvn dependency:go-offline

# Copy project source into Maven-standard layout
COPY main ./src/main
COPY test ./src/test

# Package the application
RUN mvn clean package -DskipTests

# ------------------ Final Stage ------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the packaged JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
