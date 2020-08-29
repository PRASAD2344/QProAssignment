FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY README.md README.md
RUN mvn install -DskipTests

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/QProAssignment.jar /app
EXPOSE 8080
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar QProAssignment.jar"]
