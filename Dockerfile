FROM openjdk:11.0.8-slim-buster

# RUN apt update && apt install tesseract-ocr-all
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
