# Build stage: use a Maven + JDK 24 image that matches the project configuration.
FROM maven:3.9.9-eclipse-temurin-24 AS build

WORKDIR /medidoc

# Copy only what is needed to build the artifact.
COPY pom.xml .
COPY src ./src

# Fetch dependencies first so later builds benefit from Docker layer caching.
RUN mvn dependency:go-offline
# This step creates the final fat jar through the `maven-shade-plugin` configuration in `pom.xml`.
RUN mvn clean package -DskipTests

# Runtime stage: copy the ready jar and start it with `java -jar`.
FROM eclipse-temurin:24-jre

WORKDIR /app

# Shade leaves more than one jar in `target/`, so copy the final artifact by its exact name.
COPY --from=build /medidoc/target/MediDocFX-1.0-SNAPSHOT.jar app.jar

# This starts the packaged artifact, but JavaFX still needs GUI libraries in the image.
CMD ["java", "-jar", "app.jar"]
