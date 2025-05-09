# --- Stage 1: Build the application using Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

# Build the application while caching Maven dependencies to speed up future builds
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DENV_VAR=ci -DskipTests -Dgit.skip=true

# --- Stage 2: Run the application with a minimal JRE image ---
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built WAR file from the build stage
COPY --from=build /app/target/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
