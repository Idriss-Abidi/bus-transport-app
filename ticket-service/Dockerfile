FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

# Build application and skip tests for faster image creation
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

# Copy the built jar; wildcard avoids hardcoding the exact version
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 4003

ENTRYPOINT ["java", "-jar", "app.jar"]