FROM amazoncorretto:11
LABEL maintainer="erohazakurdaev@yandex.ru"
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]