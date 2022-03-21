FROM amazoncorretto:11-alpine-jdk
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} youtube-channel-fetcher.jar
ENTRYPOINT ["java","-jar","youtube-channel-fetcher.jar"]