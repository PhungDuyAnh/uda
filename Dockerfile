FROM maven:3.8.5-openjdk-17 AS stage
WORKDIR /home
COPY . /home/
RUN mvn -f /home/pom.xml clean package

#Build an image
FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY --from=stage /home/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]
