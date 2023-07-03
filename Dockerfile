FROM openjdk:20

ENV ENVIRONMENT=prod

LABEL maintainer="zeshan.shahid@neuefische.de"

EXPOSE 8080

ADD backend/target/todoapp.jar app.jar

CMD [ "sh", "-c", "java -jar /app.jar" ]