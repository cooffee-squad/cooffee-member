FROM openjdk:17-jdk-alpine3.14

ARG JAR_FILE_PATH=./build/libs/*.jar

COPY ${JAR_FILE_PATH} member.jar

EXPOSE 28001

ENTRYPOINT ["java", "-jar", "member.jar"]
