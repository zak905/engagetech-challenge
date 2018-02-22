FROM maven:3.5.2-jdk-8-alpine

RUN apk add --no-cache curl

RUN mkdir app

EXPOSE 8090

COPY . /app

WORKDIR app

RUN mvn clean package

ENTRYPOINT ["java", "-jar","target/api-0.1.jar"]