# STAGE 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# STAGE 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Grabs only the executable jar and renames it to app.jar
COPY --from=build /app/build/libs/*[!plain].jar app.jar

# Standard Hugging Face Port configuration
EXPOSE 7860
# This forces Spring Boot to use the port Hugging Face expects
ENTRYPOINT ["java", "-Dserver.port=7860", "-jar", "app.jar"]