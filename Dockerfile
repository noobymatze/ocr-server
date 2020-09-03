FROM openjdk:11.0.8-slim-buster

RUN apt update && apt install tesseract-ocr-all --yes
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
