FROM eclipse-temurin:25-jdk-ubi10-minimal

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

CMD ["-Dspring.profiles.active=prod"]