FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Hugging Face usually listens on port 7860
EXPOSE 7860
ENV SERVER_PORT=7860

ENTRYPOINT ["java", "-jar", "app.jar"]
