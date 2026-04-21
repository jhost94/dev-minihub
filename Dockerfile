FROM openjdk:11.0.14.1-oraclelinux8

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

#ENTRYPOINT ["tail", "-f", "/dev/null"]