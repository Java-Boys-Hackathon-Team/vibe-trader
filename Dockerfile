FROM eclipse-temurin:21-jre-alpine

WORKDIR /application

COPY build/libs/*.jar vibe-trader-backend.jar

ENTRYPOINT ["java","-jar","vibe-trader-backend.jar"]
